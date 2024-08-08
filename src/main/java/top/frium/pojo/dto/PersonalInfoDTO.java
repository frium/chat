package top.frium.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @date 2024-08-08 15:50:43
 * @description
 */
@Data
@ApiModel("修改个人信息,可以选择部分不修改")
public class PersonalInfoDTO {
    @NotEmpty(message = "个人id不能为空")
    @ApiModelProperty(value = "用户id,可以用于搜索用户",required = true)
    String userId;

    @NotEmpty(message = "用户昵称不能为空")
    @ApiModelProperty(value = "用户昵称",required = true)
    String nickName;

    @NotEmpty(message = "性别不能为空")
    @ApiModelProperty(value = "性别",required = true)
    String sex;

    @Max(value = 1,message = "添加方式只能为1或0")
    @Min(value = 0,message = "添加方式只能为1或0")
    @ApiModelProperty(value = "添加方式,默认为1(需要同意后才能成为好友),0为直接添加不需要号主同意",required = true)
    Long addMethod;

    @NotNull(message = "个人签名不能为null")
    @ApiModelProperty("个性签名,可以为空")
    String personalSignature;

    @NotEmpty(message = "地区设置不能为空")
    @ApiModelProperty(value = "地区,默认中国",required = true)
    String area;
}
