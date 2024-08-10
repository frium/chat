package top.frium.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @date 2024-08-10 15:52:59
 * @description
 */
@Data
public class UserInfoVO {
    @ApiModelProperty(value = "用户id,可以用于搜索用户", required = true)
    private String userId;
    @ApiModelProperty(value = "用户昵称", required = true)
    private String nickName;
    @ApiModelProperty(value = "性别", required = true)
    private String sex;
    @ApiModelProperty(value = "添加方式,默认为1(需要同意后才能成为好友),0为直接添加不需要号主同意", required = true)
    Long addMethod;
    @ApiModelProperty("头像地址")
    private String avatar;
    @ApiModelProperty("个性签名,可以为空")
    private String personalSignature;
    @ApiModelProperty(value = "地区,默认中国", required = true)
    private String area;
}
