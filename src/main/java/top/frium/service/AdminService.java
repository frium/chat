package top.frium.service;

import top.frium.pojo.entity.UserContact;
import top.frium.pojo.entity.UserContactApply;
import top.frium.pojo.vo.UserAllInfoVO;

import java.util.List;

/**
 *
 * @date 2024-08-17 21:02:09
 * @description
 */
public interface AdminService {
    List<UserContact> getUserContact(String usrId);

    List<UserContactApply> getUserAllApply(String usrId);
    List<UserAllInfoVO> getUserInfo();

    void updateUserStatus(String userId);
}
