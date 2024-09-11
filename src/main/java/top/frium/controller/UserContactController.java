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
import top.frium.pojo.dto.MessageDTO;
import top.frium.pojo.dto.ProcessApplyDTO;
import top.frium.pojo.vo.ApplyVO;
import top.frium.pojo.vo.FriendListVO;
import top.frium.pojo.vo.UserInfoVO;
import top.frium.service.ChatMessageService;
import top.frium.service.UserContactService;
import top.frium.uitls.ChannelContextUtil;

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
    ChatMessageService chatMessageService;
    @Autowired
    UserContactService userContactService;
    @Autowired
    ChannelContextUtil channelContextUtil;

    @ApiOperation("搜索账号")
    @GetMapping("/search")
    public R<UserInfoVO> search(@NotEmpty(message = "搜索id不能为空") String searchId) {
        return R.success(userContactService.searchUser(searchId));
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

    @ApiOperation("删除好友")
    @GetMapping("/deleteFriend")
    public R<?> deleteFriend(@NotEmpty(message = "删除对象不能为空") String userId) {
        userContactService.deleteFriend(userId);
        return R.success();
    }

    @ApiOperation("拉黑联系人")
    @GetMapping("/blackoutContact")
    public R<?> blackoutContact(@RequestParam @NotEmpty(message = "拉黑对象不能为空") String userId) {
        userContactService.blackoutContact(userId);
        return R.success();
    }

    @ApiOperation("发送短信")
    @PostMapping("/sendMessage")
    public R<?> sendMessage(@Valid @ModelAttribute MessageDTO messageDTO) {
        chatMessageService.sendMessage(messageDTO);
        return R.success();
    }
    @ApiOperation("消息已读")
    @PostMapping("/readMessage")
    public R<?> readMessage(@RequestParam @NotEmpty(message = "发送消息对象不能为空") String sendUserId) {
        chatMessageService.readMessage(sendUserId);
        return R.success();
    }



}
