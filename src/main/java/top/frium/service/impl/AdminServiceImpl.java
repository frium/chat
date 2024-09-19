package top.frium.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.frium.common.MyException;
import top.frium.mapper.UserMapper;
import top.frium.pojo.dto.ManagePageDTO;
import top.frium.pojo.entity.User;
import top.frium.pojo.entity.UserContact;
import top.frium.pojo.entity.UserContactApply;
import top.frium.pojo.vo.UserAllInfoVO;
import top.frium.service.AdminService;
import top.frium.service.UserContactApplyService;
import top.frium.service.UserContactService;
import top.frium.service.UserService;

import java.util.List;

import static top.frium.common.StatusCodeEnum.USER_NOT_EXIST;
import static top.frium.context.CommonConstant.FORBIDDEN_USER;
import static top.frium.context.CommonConstant.NORMAL_USER;

/**
 *
 * @date 2024-08-17 21:02:21
 * @description
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserContactApplyService userContactApplyService;
    @Autowired
    UserContactService userContactService;

    @Override
    public List<UserAllInfoVO> getUserInfo(ManagePageDTO managePageDTO) {
        Integer pageSize = managePageDTO.getPageSize();
        int offset = (managePageDTO.getPage() - 1) * pageSize;
        return userMapper.getUserAllInfo(offset,pageSize,managePageDTO.getEmail(),managePageDTO.getUserId());
    }

    @Override
    public void updateUserStatus(String id) {
        Integer status;
        try {
            status = userService.lambdaQuery().eq(User::getId, id).select(User::getStatus).one().getStatus();
        } catch (Exception e) {
            throw new MyException(USER_NOT_EXIST);
        }
        if (FORBIDDEN_USER.equals(status)) userService.lambdaUpdate().eq(User::getId, id).set(User::getStatus,NORMAL_USER).update();
        else userService.lambdaUpdate().eq(User::getId, id).set(User::getStatus,FORBIDDEN_USER).update();
    }

    @Override
    public List<UserContact> getUserContact(String usrId) {
        return userContactService.lambdaQuery().eq(UserContact::getUserId,usrId).list();
    }

    @Override
    public List<UserContactApply> getUserAllApply(String usrId) {
      return userContactApplyService.lambdaQuery().eq(UserContactApply::getApplyUserId,usrId)
                .or().eq(UserContactApply::getReceiveUserId,usrId).list();
    }
}
