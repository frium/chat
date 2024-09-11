package top.frium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.frium.mapper.GroupInfoMapper;
import top.frium.pojo.dto.CreateGroupDTO;
import top.frium.pojo.dto.UploadGroupDTO;
import top.frium.pojo.entity.GroupInfo;
import top.frium.pojo.vo.GroupInfoVO;
import top.frium.service.GroupInfoService;
import top.frium.service.UserContactService;
import top.frium.service.UserInfoService;
import top.frium.uitls.MQUtil;

@Service
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper, GroupInfo> implements GroupInfoService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserContactService userContactService;
    @Autowired
    GroupInfoMapper groupInfoMapper;
    @Autowired
    MQUtil MQUtil;


    @Override
    public void createGroup(CreateGroupDTO createGroupDTO) {

    }

    @Override
    public void uploadGroup(UploadGroupDTO uploadGroupDTO) {

    }

    @Override
    public GroupInfoVO getGroupInfo(String groupId) {
        return null;
    }
}
