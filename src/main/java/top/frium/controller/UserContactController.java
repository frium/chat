package top.frium.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.frium.common.R;
import top.frium.pojo.dto.ApplyAddDTO;
import top.frium.pojo.dto.CreateGroupDTO;
import top.frium.service.UserContactService;

/**
 *
 * @date 2024-08-10 16:44:15
 * @description
 */
@Api("好友接口")
@Validated
@RestController
@RequestMapping("/contact")
public class UserContactController {

    @Autowired
    UserContactService userContactService;
    @GetMapping("/search")
    @ApiOperation("搜索账号")
    public R<?> search(@NotEmpty(message = "搜索id不能为空") String searchId) {
        return R.success();
    }

    @PostMapping ("/applyAdd")
    @ApiOperation("添加好友")
    public R<?> applyAdd(@RequestBody ApplyAddDTO applyAddDTO) {
        userContactService.applyAdd(applyAddDTO);
        return R.success();
    }
}
