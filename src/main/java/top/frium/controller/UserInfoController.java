package top.frium.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.frium.common.R;
import top.frium.pojo.dto.PersonalInfoDTO;
import top.frium.pojo.vo.UserInfoVO;
import top.frium.service.UserService;

/**
 *
 * @date 2024-08-11 20:33:07
 * @description
 */
@Api("个人信息接口")
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    UserService userService;
    @ApiOperation("修改个人信息")
    @PutMapping("/modifyPersonalInfo")
    public R<?> modifyPersonalInfo(@Valid @RequestBody PersonalInfoDTO personalInfoDTO) {
        userService.modifyPersonalInfo(personalInfoDTO);
        return R.success();
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/getPersonalInfo")
    public R<UserInfoVO> getPersonalInfo() {
        return R.success(userService.getPersonalInfo());
    }

    @ApiOperation("获取上次修改ID的时间")
    @GetMapping("/getLastUploadIdTime")
    public R<String> getLastUploadIdTime() {
        return R.success(userService.getLastUploadIdTime());
    }

    @ApiOperation("修改个人ID")
    @PutMapping("/modifyPersonalId")
    public R<?> modifyPersonalId(@NotEmpty(message = "修改个人id的时候提交的个人id不能为空")
                                 @RequestParam String userId) {
        userService.modifyPersonalId(userId);
        return R.success();
    }

    @ApiOperation("修改个人头像")
    @PostMapping("/uploadAvatar")
    public R<?> uploadAvatar(MultipartFile avatar) {
        userService.uploadAvatar(avatar);
        return R.success();
    }
}
