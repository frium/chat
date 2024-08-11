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
import top.frium.pojo.dto.ProcessApplyDTO;
import top.frium.pojo.vo.ApplyVO;
import top.frium.pojo.vo.FriendListVO;
import top.frium.service.UserContactService;

import java.util.List;

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

    @ApiOperation("搜索账号")
    @GetMapping("/search")
    public R<?> search(@NotEmpty(message = "搜索id不能为空") String searchId) {
        return R.success();
    }

    @ApiOperation("添加好友申请")
    @PostMapping("/applyAdd")
    public R<?> applyAdd(@Valid @RequestBody ApplyAddDTO applyAddDTO) {
        userContactService.applyAdd(applyAddDTO);
        return R.success();
    }

    @ApiOperation("获取我发送的好友申请列表")
    @GetMapping("/loadMyApply")
    public R<List<ApplyVO>> loadMyApply() {
        return R.success(userContactService.loadMyApply());
    }

    @ApiOperation("获取请求添加我为好友的申请列表")
    @GetMapping("/loadAddMeApply")
    public R<List<ApplyVO>> loadAddMeApply() {
        return R.success(userContactService.loadAddMeApply());
    }

    @ApiOperation("处理申请")
    @PostMapping("/processApply")
    public R<?> processApply(@Valid @RequestBody ProcessApplyDTO processApplyDTO) {
        userContactService.processApply(processApplyDTO);
        return R.success();
    }

    @ApiOperation("获取好友列表")
    @GetMapping("/getFriendList")
    public R<List<FriendListVO>> getFriendList() {
       return R.success(userContactService.getFriendList());
    }
}
