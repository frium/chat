package top.frium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.frium.mapper.UserInfoMapper;
import top.frium.pojo.entity.UserInfo;
import top.frium.service.UserInfoService;

/**
 *
 * @date 2024-08-08 20:19:32
 * @description
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
