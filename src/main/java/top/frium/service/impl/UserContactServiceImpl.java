package top.frium.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.mapper.UserContactApplyMapper;
import top.frium.mapper.UserContactMapper;
import top.frium.pojo.LoginUser;
import top.frium.pojo.dto.ApplyAddDTO;
import top.frium.pojo.dto.ProcessApplyDTO;
import top.frium.pojo.entity.UserContact;
import top.frium.pojo.entity.UserContactApply;
import top.frium.pojo.entity.UserInfo;
import top.frium.pojo.vo.ApplyVO;
import top.frium.pojo.vo.FriendListVO;
import top.frium.pojo.vo.UserInfoVO;
import top.frium.service.UserContactApplyService;
import top.frium.service.UserContactService;
import top.frium.service.UserInfoService;

import java.time.LocalDateTime;
import java.util.List;

import static top.frium.common.StatusCodeEnum.*;
import static top.frium.context.CommonConstant.BE_BLACKLIST;
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
    @Autowired
    UserContactApplyMapper userContactApplyMapper;
    @Autowired
    UserContactMapper userContactMapper;

    @Override
    public void applyAdd(ApplyAddDTO applyAddDTO) {
        //找是否有对应的账号
        UserInfo user = userInfoService.lambdaQuery().eq(UserInfo::getUserId, applyAddDTO.getAddId()).one();
        if (user == null) throw new MyException(StatusCodeEnum.NOT_FOUND);
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        if (applyAddDTO.getAddId().equals(userId)) throw new MyException(MYSELF);
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
                .eq(UserContactApply::getStatus, UNTREATED).count() > 0;
        if (isApply) throw new MyException(StatusCodeEnum.IS_APPLY);
        //判断添加对方是直接添加还是需要申请
        if (NO_APPLY.equals(user.getAddMethod())) {
            //更新我的好友列表
            UserContact userContact = new UserContact();
            userContact.setUserId(userId);
            userContact.setContactId(applyAddDTO.getAddId());
            userContact.setLastUpdateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
            userContact.setCreateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
            userContact.setContactType(FRIEND_CONTACT_TYPE);
            userContact.setStatus(FRIEND);
            save(userContact);
            //更新申请人的好友列表
            userContact.setContactId(userId);
            userContact.setUserId((applyAddDTO.getAddId()));
            save(userContact);
            //TODO 创建会话

            return;
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

    @Override
    public List<ApplyVO> loadMyApply() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        return userContactApplyMapper.loadMyApply(userId);
    }

    @Override
    public List<ApplyVO> loadAddMeApply() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        return userContactApplyMapper.loadAddMeApply(userId);
    }

    @Override
    public void processApply(ProcessApplyDTO processApplyDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();    //判断是否有请求没被处理
        Integer selection = processApplyDTO.getSelection();
        boolean flag = userContactApplyService.lambdaQuery().eq(UserContactApply::getApplyUserId, processApplyDTO.getApplyId())
                .eq(UserContactApply::getReceiveUserId, userId).eq(UserContactApply::getStatus, UNTREATED).count() > 0;
        if (!flag || UNTREATED.equals(selection)) throw new MyException(StatusCodeEnum.NOT_FOUND);
        //修改申请状态
        userContactApplyService.lambdaUpdate().eq(UserContactApply::getApplyUserId, processApplyDTO.getApplyId())
                .eq(UserContactApply::getReceiveUserId, userId)
                .set(UserContactApply::getStatus, selection)
                .update();
        if (AGREE.equals(selection)) {
            //更新我的好友列表
            UserContact userContact = new UserContact();
            userContact.setUserId(userId);
            userContact.setContactId(processApplyDTO.getApplyId());
            userContact.setLastUpdateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
            userContact.setCreateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
            userContact.setContactType(FRIEND_CONTACT_TYPE);
            userContact.setStatus(FRIEND);
            save(userContact);
            //更新申请人的好友列表
            userContact.setContactId(userId);
            userContact.setUserId(processApplyDTO.getApplyId());
            save(userContact);

            //TODO 创建会话
        }

    }

    @Override
    public List<FriendListVO> getFriendList() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        return userContactMapper.getFriend(userId);

    }

    @Override
    public void deleteFriend(String contactId) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        //判断是否是自己
        if (contactId.equals(userId)) throw new MyException(MYSELF);
        //判断是否是好友
        UserContact contact = lambdaQuery().eq(UserContact::getUserId, userId)
                .eq(UserContact::getContactId, contactId)
                .select(UserContact::getStatus).one();
        if (contact == null) throw new MyException(NOT_FOUND);
        Integer status = contact.getStatus();
        if (DELETE.equals(status)) throw new MyException(DELETED);
        //如果已经被删除 互删,修改状态为非好友
        if (BE_DELETE.equals(status)) {
            lambdaUpdate().eq(UserContact::getUserId, userId)
                    .eq(UserContact::getContactId, contactId)
                    .set(UserContact::getStatus, NOT_FRIEND).update();
            lambdaUpdate().eq(UserContact::getContactId, userId)
                    .eq(UserContact::getUserId, contactId)
                    .set(UserContact::getStatus, NOT_FRIEND).update();
            return;
        }
        //修改双方的联系人表
        lambdaUpdate().eq(UserContact::getUserId, userId)
                .eq(UserContact::getContactId, contactId)
                .set(UserContact::getStatus, DELETE).update();
        lambdaUpdate().eq(UserContact::getContactId, userId)
                .eq(UserContact::getUserId, contactId)
                .set(UserContact::getStatus, BE_DELETE).update();
    }

    @Override
    public void blackoutContact(String contactId) {
        //判断是否拉黑
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        //判断是否是自己
        if (contactId.equals(userId)) throw new MyException(MYSELF);
        UserContact contact = lambdaQuery().eq(UserContact::getUserId, userId)
                .eq(UserContact::getContactId, contactId)
                .select(UserContact::getStatus).one();
        if (contact == null) throw new MyException(NOT_FOUND);
        Integer status = contact.getStatus();
        if (BLACKLIST.equals(status)) throw new MyException(BLACKED_OUT);
        //如果没有被拉黑
        if (!BE_BLACKLIST.equals(status)) {
            lambdaUpdate().eq(UserContact::getUserId, userId)
                    .eq(UserContact::getContactId, contactId)
                    .set(UserContact::getStatus, BLACKLIST).update();
        }
        lambdaUpdate().eq(UserContact::getContactId, userId)
                .eq(UserContact::getUserId, contactId)
                .set(UserContact::getStatus, BE_BLACKLIST).update();
    }

    @Override
    public UserInfoVO searchUser(String searchId) {
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getUserId, searchId).one();
        if (userInfo == null) throw new MyException(USER_NOT_EXIST);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        return userInfoVO;
    }


}
