package top.frium.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @date 2024-08-11 16:37:51
 * @description
 */
@Data
@ApiModel("申请列表")
public class ApplyVO {
    @ApiModelProperty(value = "申请对象的具体信息",required = true)
    private UserInfoVO userInfoVO;

    @ApiModelProperty(value = "申请时间",required = true)
    private String applyTime;

    @ApiModelProperty(value = "申请状态,-1拒绝 0未处理 1同意",required = true)
    private Integer status;

    @ApiModelProperty(value = "申请信息",required = true)
    private String applyInfo;
}
