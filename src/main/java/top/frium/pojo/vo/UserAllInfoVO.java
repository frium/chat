package top.frium.pojo.vo;

import lombok.Data;

/**
 *
 * @date 2024-08-15 21:34:25
 * @description
 */
@Data
public class UserAllInfoVO {
    private Long id;
    private String password;
    private String email;
    private String userId;
    private String nickName;
    private String sex;
    private Integer addMethod;
    private String personalSignature;
    private String avatar;
    private String createTime;
    private String lastLoginTime;
    private String lastOffTime;
    private String area;
    private String ip;
    private String ipAddress;
    private String userIdLastUpdateTime;
}
