package top.frium.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @date 2024-09-11 12:08:00
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMessageVO {
    @ApiModelProperty("发送消息用户id")
    @NotEmpty(message = "发送消息的用户id不为空")
    private String sendUserId;

    @ApiModelProperty("发送内容,文件或者文本")
    @NotEmpty(message = "发送内容不为空")
    private Object content;

    @ApiModelProperty("发送时间")
    @NotEmpty(message = "发送时间不为空")
    private String sendTime;

    @ApiModelProperty("文件名称")
    @NotEmpty(message = "文件名不为空")
    private String fileName;

    @ApiModelProperty("文件类型")
    @NotEmpty(message = "文件类型")
    private String fileType;
}
