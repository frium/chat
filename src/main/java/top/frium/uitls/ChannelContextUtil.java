package top.frium.uitls;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.pojo.LoginUser;
import top.frium.pojo.dto.MessageDTO;
import top.frium.pojo.entity.ChatMessage;
import top.frium.pojo.entity.UserInfo;
import top.frium.pojo.vo.ApplyMessageVO;
import top.frium.pojo.vo.FileMessageVO;
import top.frium.pojo.vo.MessageVO;
import top.frium.pojo.vo.UserInfoVO;
import top.frium.service.UserInfoService;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import static top.frium.common.StatusCodeEnum.*;
import static top.frium.context.CommonConstant.*;


/**
 *
 * @date 2024-09-03 16:43:26
 * @description
 */
@Slf4j
@Component
public class ChannelContextUtil {
    @Autowired
    MQUtil mqUtil;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;


    private static final ConcurrentHashMap<String, Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();

    public void addContext(String userId, Channel channel) {
        String channelId = channel.id().toString();
        AttributeKey<Object> attributeKey;
        if (!AttributeKey.exists(channelId)) attributeKey = AttributeKey.newInstance(channelId);
        else attributeKey = AttributeKey.valueOf(channelId);
        channel.attr(attributeKey).set(userId);
        USER_CONTEXT_MAP.put(userId, channel);
        log.info("用户{}连接成功", userId);
    }

    public void removeContext(Channel channel) {
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        //更新用户最后的离线时间
        userInfoService.lambdaUpdate().set(UserInfo::getLastOffTime, LocalDateTime.now().format(DATA_TIME_PATTERN)).eq(UserInfo::getUserId, userId).update();
    }

    public void sendApply(String receiveId,String applyInfo){
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        ApplyMessageVO applyMessageVO=new ApplyMessageVO();
        applyMessageVO.setApplyTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        applyMessageVO.setApplyInfo(applyInfo);
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getUserId, userId).one();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo,userInfoVO);
        applyMessageVO.setUserInfoVO(userInfoVO);
        Channel channel = USER_CONTEXT_MAP.get(receiveId);
        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(applyMessageVO)));
    }

    public ChatMessage sendMsg(MessageDTO messageDTO) {
        String receiveId = messageDTO.getReceiveUserId();
        if (receiveId == null) throw new MyException(StatusCodeEnum.USER_NOT_EXIST);
        ChatMessage chatMessage = new ChatMessage();
        Object content = messageDTO.getContent();
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        Channel channel = USER_CONTEXT_MAP.get(receiveId);
        String now = LocalDateTime.now().format(DATA_TIME_PATTERN);
        boolean online = channel != null;
        //持久化处理
        chatMessage.setSendUserId(userId);
        chatMessage.setReceiveUserId(receiveId);
        chatMessage.setStatus(UNREAD);
        chatMessage.setSendTime(now);
        if (content instanceof String string) {
            if (string.length() > MAX_MESSAGE_SIZE) throw new MyException(CONTENT_TOO_LONG);
            MessageVO messageVO = new MessageVO();
            messageVO.setContent(string);
            messageVO.setSendTime(now);
            messageVO.setSendUserId(userId);
            chatMessage.setMessageType(NORMAL_MESSAGE);
            chatMessage.setMessageContent(string);
            if (online) channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageVO)));
        } else if (content instanceof MultipartFile file) {
            if (file.getSize() > MAX_FILE_SIZE) throw new MyException(FILE_TOO_BIG);
            FileMessageVO fileMessageVO = new FileMessageVO();
            fileMessageVO.setFileName(file.getOriginalFilename());
            fileMessageVO.setFileType(file.getContentType());
            fileMessageVO.setSendTime(now);
            fileMessageVO.setSendUserId(userId);
            chatMessage.setMessageType(FILE);
            //获取原本文件的文件名以及文件类型
            chatMessage.setFileName(file.getName());
            chatMessage.setFileType(file.getContentType());
            //mq异步传输文件
            try {
                String fileAddress = mqUtil.sendFileMessage(file);
                chatMessage.setFileAddress(fileAddress);
                String base64Content = Base64.getEncoder().encodeToString(file.getBytes());
                fileMessageVO.setContent(base64Content);
                if (online) channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(fileMessageVO)));
            } catch (Exception e) {
                throw new MyException(ERROR);
            }
        }
        return chatMessage;
    }


}
