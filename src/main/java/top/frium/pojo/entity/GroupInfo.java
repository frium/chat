package top.frium.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 *
 * @date 2024-08-09 15:21:12
 * @description
 */
@Data
public class GroupInfo {
    @TableId(value = "group_id", type = IdType.AUTO)
    private Long groupId;

    private String groupName;

    private String groupOwnerId;

    private String groupNotice;

    private Integer joinType;

    private String coverImage;

    private String createTime;

    private String lastUpdateTime;

    private Integer status;
}
