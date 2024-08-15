package top.frium.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.frium.common.R;
import top.frium.pojo.dto.LoginEmailDTO;
import top.frium.pojo.vo.UserAllInfoVO;
import top.frium.service.UserService;

import java.util.List;

/**
 *
 * @date 2024-08-15 20:39:28
 * @description
 */
@Api("管理员接口")
@Validated
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('admin')")
public class AdminController {

    @Autowired
    UserService userService;

    @ApiOperation("登录")
    @PostMapping("/loginByEmail")
    @PreAuthorize("permitAll()")
    public R<?> loginByEmail(@Valid @RequestBody LoginEmailDTO loginEmailDTO) {
        return R.success(userService.loginByEmail(loginEmailDTO));
    }

    @ApiOperation("登出")
    @GetMapping("/logout")
    public R<?> logout() {
        userService.logout();
        return R.success();
    }

    //TODO 用es试试
    @ApiOperation("获取用户信息")
    @PostMapping("/getUserInfo")
    public R<List<UserAllInfoVO>> getUserInfo() {
        return R.success(userService.getUserInfo());
    }

    @ApiOperation("禁用用户")
    @GetMapping("/forbidUser")
    public R<?> forbidUser(String userId) {
        return R.success();
    }

    @ApiOperation("恢复禁用用户")
    @GetMapping("/RestoreUser")
    public R<?> RestoreUser(String userId) {
        return R.success();
    }

}
