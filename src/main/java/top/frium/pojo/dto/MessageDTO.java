package top.frium.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @date 2024-09-03 20:27:04
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发送消息")
public class MessageDTO {
    @ApiModelProperty("接收消息用户id(userId)")
    @NotEmpty(message = "接受消息的用户id不能为空")
    private String receiveUserId;
    @ApiModelProperty("发送的文本内容")
    private Object content;
}
