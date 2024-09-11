package top.frium.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 * @date 2024-08-11 18:17:04
 * @description
 */
@Data
@ApiModel("处理消息")
public class ProcessApplyDTO {

    @ApiModelProperty("发送请求的人的userId")
    @NotEmpty(message = "请求人的id不能为空")
    private String applyUserId;

    @ApiModelProperty("我的选择,-1拒绝,1同意")
    @Max(value = 1,message = "选择范围为-1~1")
    @Min(value = -1,message = "选择范围为-1~1")
    private Integer selection;
}
