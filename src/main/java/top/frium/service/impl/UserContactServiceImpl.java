package top.frium.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.mapper.UserContactMapper;
import top.frium.pojo.LoginUser;
import top.frium.pojo.dto.ApplyAddDTO;
import top.frium.pojo.entity.UserContact;
import top.frium.pojo.entity.UserContactApply;
import top.frium.pojo.entity.UserInfo;
import top.frium.service.UserContactApplyService;
import top.frium.service.UserContactService;
import top.frium.service.UserInfoService;

import java.time.LocalDateTime;

import static top.frium.context.CommonConstant.*;

/**
 * @author frium
 * @since 2024-08-09
 */
@Service
public class UserContactServiceImpl extends ServiceImpl<UserContactMapper, UserContact> implements UserContactService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserContactApplyService userContactApplyService;

    @Override
    public void applyAdd(ApplyAddDTO applyAddDTO) {
        //找是否有对应的账号
        UserInfo user = userInfoService.lambdaQuery().eq(UserInfo::getUserId, applyAddDTO.getAddId()).one();
        if (user == null) throw new MyException(StatusCodeEnum.NOT_FOUND);
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        //查询对方是否已经添加,判断是否被拉黑
        UserContact contact = lambdaQuery().eq(UserContact::getUserId, userId).eq(UserContact::getContactId, applyAddDTO.getAddId())
                .select(UserContact::getContactType).one();
        if (contact != null) {
            Integer contactType = contact.getContactType();
            if (BE_BLACKLIST.equals(contactType)) throw new MyException(StatusCodeEnum.BE_BLACKLIST);
            if (FRIEND.equals(contactType)) throw new MyException(StatusCodeEnum.IS_FRIEND);
        }
        //查询有没有发过申请,或发过的申请是否被处理,发过就不能再发
        boolean isApply = userContactApplyService.lambdaQuery().eq(UserContactApply::getApplyUserId, userId)
                .eq(UserContactApply::getReceiveUserId, applyAddDTO.getAddId())
                .eq(UserContactApply::getStatus,UNTREATED).count() > 0;
        if (isApply) throw new MyException(StatusCodeEnum.IS_APPLY);
        //判断添加对方是直接添加还是需要申请
        if (NEED_APPLY.equals(user.getAddMethod())) {
            //TODO 直接添加联系人
        }
        //设置发送请求的话术如果没有
        String nickName = userInfoService.lambdaQuery().eq(UserInfo::getUserId, userId)
                .select(UserInfo::getNickName).one().getNickName();
        String applyMessage = !applyAddDTO.getApplyMessage().isEmpty() ? applyAddDTO.getApplyMessage()
                : DEFAULT_ADD_MESSAGE.formatted(nickName);
        //发送请求
        UserContactApply userContactApply = new UserContactApply();
        userContactApply.setApplyUserId(userId);
        userContactApply.setReceiveUserId(user.getUserId());
        userContactApply.setContactType(FRIEND_CONTACT_TYPE);
        userContactApply.setApplyTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        userContactApply.setStatus(UNTREATED);
        userContactApply.setApplyInfo(applyMessage);
        userContactApplyService.save(userContactApply);
        //TODO 发送ws消息,告诉对方有好友添加消息

    }
}
