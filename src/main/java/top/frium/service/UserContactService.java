package top.frium.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.frium.pojo.dto.ApplyAddDTO;
import top.frium.pojo.dto.ProcessApplyDTO;
import top.frium.pojo.entity.UserContact;
import top.frium.pojo.vo.ApplyVO;
import top.frium.pojo.vo.FriendListVO;
import top.frium.pojo.vo.UserInfoVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frium
 * @since 2024-08-09
 */
public interface UserContactService extends IService<UserContact> {

    void applyAdd(ApplyAddDTO applyAddDTO);

    List<ApplyVO> loadMyApply();

    List<ApplyVO>loadAddMeApply();

    void processApply(ProcessApplyDTO processApplyDTO);

    List<FriendListVO> getFriendList();

    void deleteFriend(String userId);

    void blackoutContact(String userId);

    UserInfoVO searchUser(String searchId);
}
