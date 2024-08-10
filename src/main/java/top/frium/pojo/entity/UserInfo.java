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
    private Long id;
    private String userId;
    private String nickName;
    private String sex;
    private Long addMethod;
    private String personalSignature;
    private String avatar;
    private String createTime;
    private String lastLoginTime;
    private String lastOffTime;
    private String area;
    private String ip;
    private String ipAddress;

}
