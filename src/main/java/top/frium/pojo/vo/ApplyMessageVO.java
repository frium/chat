package top.frium.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @date 2024-09-11 15:17:35
 * @description
 */
@Data
public class ApplyMessageVO {
    @ApiModelProperty(value = "申请对象的具体信息", required = true)
    private UserInfoVO userInfoVO;

    @ApiModelProperty(value = "申请时间", required = true)
    private String applyTime;

    @ApiModelProperty(value = "申请信息", required = true)
    private String applyInfo;
}
