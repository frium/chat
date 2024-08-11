package top.frium.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @date 2024-08-11 19:07:49
 * @description
 */
@Data
public class FriendListVO {
    @ApiModelProperty(value = "用户id,可以用于搜索用户", required = true)
    private String userId;
    @ApiModelProperty(value = "用户昵称", required = true)
    private String nickName;
    @ApiModelProperty(value = "头像地址",required = true)
    private String avatar;
}
