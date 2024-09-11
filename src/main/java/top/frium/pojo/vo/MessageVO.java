package top.frium.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @date 2024-09-10 19:18:58
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("接受消息")
public class MessageVO {
    @ApiModelProperty("发送消息用户id")
    @NotEmpty(message = "发送消息的用户id不为空")
    private String sendUserId;

    @ApiModelProperty("发送内容,文件或者文本")
    @NotEmpty(message = "发送内容不为空")
    private Object content;

    @ApiModelProperty("发送时间")
    @NotEmpty(message = "发送时间不为空")
    private String sendTime;
}
