package top.frium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.mapper.GroupInfoMapper;
import top.frium.pojo.LoginUser;
import top.frium.pojo.dto.CreateGroupDTO;
import top.frium.pojo.dto.UploadGroupDTO;
import top.frium.pojo.entity.GroupInfo;
import top.frium.pojo.entity.UserContact;
import top.frium.pojo.entity.UserInfo;
import top.frium.pojo.vo.GroupInfoVO;
import top.frium.pojo.vo.UserInfoVO;
import top.frium.service.GroupInfoService;
import top.frium.service.UserContactService;
import top.frium.service.UserInfoService;
import top.frium.uitls.ImageMQUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static top.frium.context.CommonConstant.*;

@Service
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper, GroupInfo> implements GroupInfoService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserContactService userContactService;
    @Autowired
    GroupInfoMapper groupInfoMapper;
    @Autowired
    ImageMQUtil imageMQUtil;


    @Override
    public void createGroup(CreateGroupDTO createGroupDTO) {
        GroupInfo groupInfo = new GroupInfo();
        BeanUtils.copyProperties(createGroupDTO, groupInfo);
        MultipartFile image = createGroupDTO.getCoverImage();
        String fileName;
        try {
            fileName = imageMQUtil.sendMessage(image);
        } catch (IOException e) {
            throw new MyException(StatusCodeEnum.ERROR);
        }
        groupInfo.setCoverImage(fileName);
        //设置自己为群主
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        groupInfo.setGroupOwnerId(userId);
        groupInfo.setCreateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        groupInfo.setLastUpdateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        groupInfo.setStatus(GROUP_NORMAL_STATUS);
        save(groupInfo);

        //将群设为自己的联系人
        UserContact userContact = new UserContact();
        userContact.setUserId(userId);
        userContact.setStatus(FRIEND);
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
        if (groupInfo == null) throw new MyException(StatusCodeEnum.NOT_FOUND);
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        if (!Objects.equals(groupInfo.getGroupOwnerId(), userId)) throw new MyException(StatusCodeEnum.NO_PERMISSION);
        //更新相关信息
        GroupInfo updateGroupInfo = new GroupInfo();
        updateGroupInfo.setLastUpdateTime(LocalDateTime.now().format(DATA_TIME_PATTERN));
        BeanUtils.copyProperties(uploadGroupDTO, updateGroupInfo);
        lambdaUpdate().eq(GroupInfo::getGroupId, uploadGroupDTO.getGroupId()).update(updateGroupInfo);

        //TODO 发送消息修改群名称
    }

    @Override
    public GroupInfoVO getGroupInfo(String groupId) {
        //判断当前发起请求的人是否是群聊内的人
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        UserContact contact = userContactService.lambdaQuery()
                .eq(UserContact::getUserId, userId)
                .eq(UserContact::getContactId, groupId)
                .select(UserContact::getContactType).one();
        if (contact == null || FRIEND_CONTACT_TYPE.equals(contact.getContactType()))
            throw new MyException(StatusCodeEnum.NOT_FOUND);
        //查询具体信息
        GroupInfo groupInfo = lambdaQuery().eq(GroupInfo::getGroupId, groupId).one();
        if (groupInfo == null || GROUP_FORBIDDEN_STATUS.equals(groupInfo.getStatus()))
            throw new MyException(StatusCodeEnum.NOT_FOUND);
        //查询成员数量
        Long memberNumber = userContactService.lambdaQuery().eq(UserContact::getContactType, GROUP_CONTACT_TYPE)
                .eq(UserContact::getContactId, groupId).count();
        //获取群主的详细信息
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getUserId, groupInfo.getGroupOwnerId()).one();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        GroupInfoVO groupInfoVO = new GroupInfoVO();
        BeanUtils.copyProperties(groupInfo, groupInfoVO);
        groupInfoVO.setMemberNumber(memberNumber);
        groupInfoVO.setUserInfoVO(userInfoVO);
        return groupInfoVO;
    }
}
