package top.frium.pojo.entity;

import lombok.Data;

/**
 *
 * @date 2024-08-09 15:21:12
 * @description
 */
@Data
public class GroupInfo {
    Long groupId;

    String groupName;

    String groupOwnerId;

    String groupNotice;

    Integer joinType;

    String coverImage;

    String createTime;

    Integer status;
}
