package top.frium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.context.BaseContext;
import top.frium.mapper.UserMapper;
import top.frium.pojo.LoginUser;
import top.frium.pojo.dto.ForgetPasswordDTO;
import top.frium.pojo.dto.LoginEmailDTO;
import top.frium.pojo.dto.PersonalInfoDTO;
import top.frium.pojo.dto.RegisterEmailDTO;
import top.frium.pojo.entity.User;
import top.frium.pojo.entity.UserInfo;
import top.frium.pojo.vo.UserInfoVO;
import top.frium.service.UserInfoService;
import top.frium.service.UserService;
import top.frium.uitls.EmailUtil;
import top.frium.uitls.JwtUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static top.frium.context.CommonConstant.*;
import static top.frium.uitls.IpUtil.getIpSource;

/**
 *
 * @date 2024-07-29 23:30:21
 * @description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    AuthenticationManager authenticationManager;
    @Value("${jwt.key}")
    String key;
    @Value("${jwt.ttl}")
    Long ttl;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserInfoService userInfoService;

    @Override
    public void getEmailSMS(String email) {
        emailUtil.sendVerificationEmail(email);
    }

    @Override
    public void registerByEmail(RegisterEmailDTO registerEmailDTO) {
        //在redis中检查是否数据匹配
        Object o = redisTemplate.opsForValue().get(registerEmailDTO.getEmail());
        if (o == null || !registerEmailDTO.getVerify().equals(o.toString())) {
            throw new MyException(StatusCodeEnum.ERROR_VERIFY);
        }
        //判断是否重复注册
        User u = lambdaQuery().eq(User::getEmail, registerEmailDTO.getEmail()).one();
        if (u != null) throw new MyException(StatusCodeEnum.USER_EXIST);
        //加密密码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        registerEmailDTO.setPassword(bCryptPasswordEncoder.encode(registerEmailDTO.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(registerEmailDTO, user);
        save(user);
        UUID uuid = UUID.randomUUID();
        String now = LocalDateTime.now().format(DATA_TIME_PATTERN);
        String currentIp = BaseContext.getCurrentIp();
        UserInfo userInfo = new UserInfo(
                user.getId(),
                String.valueOf(uuid),
                USERNAME_PREFIX + uuid,
                DEFAULT_SEX,
                DEFAULT_ADDITION_METHOD,
                DEFAULT_PERSONAL_SIGNATURE,
                DEFAULT_AVATAR,
                now, null, null,
                DEFAULT_AREA, currentIp, getIpSource(currentIp)
        );
        userInfoService.save(userInfo);
        userMapper.addUserPermission(user.getId());
    }

    @Override
    public String loginByEmail(LoginEmailDTO loginEmailDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginEmailDTO.getEmail(), loginEmailDTO.getPassword());
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new MyException(StatusCodeEnum.LOGIN_FAIL);
        }
        //存token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long id = loginUser.getUser().getId();
        //修改登录时间
        userInfoService.lambdaUpdate().eq(UserInfo::getId, id)
                .set(UserInfo::getLastLoginTime, LocalDateTime.now().format(DATA_TIME_PATTERN)).update();
        redisTemplate.opsForValue().set(LOGIN_USER + id, loginUser, 7, TimeUnit.DAYS);
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, id);
        return JwtUtil.createToken(key, ttl, claims);
    }


    @Override
    public void forgetPassword(ForgetPasswordDTO forgetPasswordDTO) {
        Object o = redisTemplate.opsForValue().get(forgetPasswordDTO.getEmail());
        if (o == null || !forgetPasswordDTO.getVerify().equals(o.toString())) {
            throw new MyException(StatusCodeEnum.ERROR_VERIFY);
        }
        boolean update = lambdaUpdate().eq(User::getEmail, forgetPasswordDTO.getEmail())
                .set(User::getPassword, forgetPasswordDTO.getNewPassword()).update();
        if (!update) throw new MyException(StatusCodeEnum.USER_NOT_EXIST);
    }

    @Override
    public void modifyPersonalInfo(PersonalInfoDTO personalInfoDTO) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(personalInfoDTO, userInfo);
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userInfoService.lambdaUpdate().eq(UserInfo::getId, loginUser.getUser().getId()).update(userInfo);
    }

    @Override
    public void logout() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisTemplate.delete(LOGIN_USER + loginUser.getUser().getId());
        //修改下线时间
        userInfoService.lambdaUpdate().eq(UserInfo::getId, loginUser.getUser().getId())
                .set(UserInfo::getLastOffTime, LocalDateTime.now().format(DATA_TIME_PATTERN)).update();
    }

    @Override
    public UserInfoVO getPersonalInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getId, loginUser.getUser().getId()).one();
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        return userInfoVO;
    }

    @Override
    public void uploadAvatar(MultipartFile avatar) {
        //TODO 修改头像
    }
}
