package top.frium.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.frium.pojo.dto.CreateGroupDTO;
import top.frium.pojo.dto.UploadGroupDTO;
import top.frium.pojo.entity.GroupInfo;
import top.frium.pojo.vo.GroupInfoVO;


/**
 * @author author
 * @since 2024-08-09
 */
public interface GroupInfoService extends IService<GroupInfo> {
  void createGroup(CreateGroupDTO createGroupDTO);

  void uploadGroup(UploadGroupDTO uploadGroupDTO);

    GroupInfoVO getGroupInfo(String groupId);
}
