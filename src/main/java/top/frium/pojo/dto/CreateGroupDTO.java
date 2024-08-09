package top.frium.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @date 2024-08-09 15:10:41
 * @description
 */
@Data
@ApiModel("群聊")
public class CreateGroupDTO {
    @NotEmpty(message = "群聊名字不能为空")
    @ApiModelProperty(value = "群聊名称",required = true)
    String groupName;

    @NotNull(message = "群公告不能为null")
    @ApiModelProperty("群公告,可为空")
    String groupNotice;

    @Max(value = 1,message = "添加方式只能为1或0")
    @Min(value = 0,message = "添加方式只能为1或0")
    @ApiModelProperty(value = "0直接加入 1需要群主同意后加入",required = true)
    Integer joinType;

    @ApiModelProperty(value = "群聊封面(头像)",required = true)
    MultipartFile coverImage;
}