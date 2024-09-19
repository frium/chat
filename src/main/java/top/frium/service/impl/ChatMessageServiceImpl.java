package top.frium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.mapper.ChatMessageMapper;
import top.frium.pojo.LoginUser;
import top.frium.pojo.dto.MessageDTO;
import top.frium.pojo.entity.ChatMessage;
import top.frium.pojo.entity.UserContact;
import top.frium.pojo.vo.ChatListVO;
import top.frium.service.ChatMessageService;
import top.frium.service.UserContactService;
import top.frium.uitls.ChannelContextUtil;

import java.util.List;
import java.util.Objects;

import static top.frium.context.CommonConstant.FRIEND;
import static top.frium.context.CommonConstant.READ;

/**
 *
 * @date 2024-09-03 19:53:14
 * @description
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {
    @Autowired
    ChannelContextUtil channelContextUtil;
    @Autowired
    UserContactService userContactService;
    @Autowired
    ChatMessageMapper chatMessageMapper;

    @Override
    public void readMessage(String sendUserId) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        lambdaUpdate().eq(ChatMessage::getReceiveUserId, userId).eq(ChatMessage::getSendUserId, sendUserId)
                .set(ChatMessage::getStatus, READ).update();
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        //判断和当前用户的关系
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        String receiveUserId = messageDTO.getReceiveUserId();
        UserContact contact = userContactService.lambdaQuery().eq(UserContact::getUserId, userId).eq(UserContact::getContactId, receiveUserId)
                .select(UserContact::getStatus).one();
        if (contact == null) throw new MyException(StatusCodeEnum.NOT_FOUND);
        Integer status = contact.getStatus();
        if (!Objects.equals(status, FRIEND)) throw new MyException(StatusCodeEnum.NO_FRIEND);
        save(channelContextUtil.sendMsg(messageDTO));
    }


    @Override
    public List<ChatListVO> getChatList() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        System.out.println(userId);
        //先获取当前用户的所有好友 再通过用户id和好友id获取最后的聊天信息,以及好友的具体信息
        return chatMessageMapper.getChatList(userId);
    }
}
