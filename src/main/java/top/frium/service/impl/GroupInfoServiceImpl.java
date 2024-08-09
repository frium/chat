package top.frium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.mapper.GroupInfoMapper;
import top.frium.pojo.LoginUser;
import top.frium.pojo.dto.CreateGroupDTO;
import top.frium.pojo.dto.UploadGroupDTO;
import top.frium.pojo.entity.GroupInfo;
import top.frium.pojo.entity.UserContact;
import top.frium.service.GroupInfoService;
import top.frium.service.UserContactService;
import top.frium.service.UserInfoService;

import java.time.LocalDateTime;
import java.util.Objects;

import static top.frium.context.CommonConstant.*;

@Service
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper, GroupInfo> implements GroupInfoService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserContactService userContactService;

    @Override
    public void createGroup(CreateGroupDTO createGroupDTO) {
        //TODO 将图片存入本地
        GroupInfo groupInfo = new GroupInfo();
        BeanUtils.copyProperties(createGroupDTO, groupInfo);
        groupInfo.setCoverImage("https://blog.frium.top/upload/%E6%B5%81%E8%90%A4.jpg");
        //设置自己为群主
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfoService.getById(loginUser.getUser().getId()).getUserId();
        groupInfo.setGroupOwnerId(userId);
        groupInfo.setCreateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        groupInfo.setStatus(GROUP_NORMAL_STATUS);
        save(groupInfo);

        //将群设为自己的联系人
        UserContact userContact=new UserContact();
        userContact.setUserId(userId);
        userContact.setStatus(CONTACT_FRIEND);
        userContact.setContactType(GROUP_CONTACT_TYPE);
        userContact.setCreateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        userContact.setLastUpdateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        userContact.setContactId(String.valueOf(groupInfo.getGroupId()));
        userContactService.save(userContact);

        //TODO 创建会话  发送消息
    }

    @Override
    public void uploadGroup(UploadGroupDTO uploadGroupDTO) {
        //判断发起人是否是群主
        GroupInfo groupInfo = lambdaQuery().eq(GroupInfo::getGroupId, uploadGroupDTO.getGroupId()).one();
        if(groupInfo ==null) throw new MyException(StatusCodeEnum.NOT_FOUND);
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfoService.getById(loginUser.getUser().getId()).getUserId();
        if(!Objects.equals(groupInfo.getGroupOwnerId(), userId)) throw new MyException(StatusCodeEnum.NO_PERMISSION);
        //更新相关信息
        GroupInfo updateGroupInfo =new GroupInfo();
        BeanUtils.copyProperties(uploadGroupDTO, updateGroupInfo);
        lambdaUpdate().eq(GroupInfo::getGroupId, uploadGroupDTO.getGroupId()).update(updateGroupInfo);

        //TODO 发送消息修改群名称
    }
}
