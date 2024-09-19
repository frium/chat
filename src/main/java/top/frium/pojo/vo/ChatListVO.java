package top.frium.pojo.vo;

import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @date 2024-09-18 11:55:29
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("聊天列表")
public class ChatListVO {
    @NotNull(message = "聊天的最后一句话")
    String lastMessage;
    @NotNull(message = "最后发送消息的时间")
    String lastSendTime;
    @NotNull(message = "头像的地址")
    String contactAvtar;
    @NotNull(message = "联系人的userId")
    String contactId;
    @NotNull(message = "联系人的昵称")
    String contactNickName;
    @NotNull(message = "消息类型")
    Integer messageType;
    @NotNull(message = "文件名称")
    String fileName;
}
