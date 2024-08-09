package top.frium.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.frium.common.R;
import top.frium.pojo.dto.CreateGroupDTO;
import top.frium.pojo.dto.UploadGroupDTO;
import top.frium.service.GroupInfoService;

/**
 *
 * @date 2024-08-09 15:05:49
 * @description
 */
@Api("群聊接口")
@Validated
@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupInfoService groupInfoService;

    @ApiOperation("创建群聊")
    @PostMapping("/createGroup")
    public R<?> createGroup(@Valid @ModelAttribute CreateGroupDTO createGroupDTO) {
        groupInfoService.createGroup(createGroupDTO);
        return R.success();
    }


    @ApiOperation("修改群聊信息")
    @PostMapping("/uploadGroup")
    public R<?> uploadGroup(@Valid @ModelAttribute UploadGroupDTO uploadGroupDTO) {
        groupInfoService.uploadGroup(uploadGroupDTO);
        return R.success();
    }

}
