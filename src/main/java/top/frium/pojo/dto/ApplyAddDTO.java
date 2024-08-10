package top.frium.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @date 2024-08-10 16:57:11
 * @description
 */
@Data
public class ApplyAddDTO {
    @NotEmpty(message = "添加id不能为空")
    @ApiModelProperty(value = "添加对象的id",required = true)
    String addId;

    @NotNull(message = "申请信息不能为null")
    @ApiModelProperty("申请信息,可以为空")
    String applyMessage;

}
