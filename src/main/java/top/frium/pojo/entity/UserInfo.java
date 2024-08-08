package top.frium.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @date 2024-08-08 15:06:49
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    Long id;
    String userId;
    String nickName;
    String sex;
    Long addMethod;
    String personalSignature;
    String createTime;
    String lastLoginTime;
    String lastOffTime;
    String area;
    String ip;
    String ipAddress;

}
