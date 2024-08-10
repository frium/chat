package top.frium.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @date 2024-08-10 15:22:22
 * @description
 */
@Data
@ApiModel("群聊的详细信息")
public class GroupInfoVO {
    @ApiModelProperty(value = "群的唯一标识id",required = true)
    private Long groupId;

    @ApiModelProperty(value = "群名",required = true)
    private String groupName;

    @ApiModelProperty(value = "群主信息",required = true)
    private UserInfoVO userInfoVO;

    @ApiModelProperty(value = "群公告",required = true)
    private String groupNotice;

    @ApiModelProperty(value = "进群方式 0直接加入 1需要群主同意后加入",required = true)
    private Integer joinType;

    @ApiModelProperty(value = "群封面",required = true)
    private String coverImage;

    @ApiModelProperty(value = "群成员数量",required = true)
    private Long memberNumber;

}
