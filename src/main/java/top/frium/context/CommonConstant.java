package top.frium.context;

import java.time.format.DateTimeFormatter;

/**
 *
 * @date 2024-07-31 14:51:40
 * @description
 */
public interface CommonConstant {
    String LOGIN_USER = "loginUser";
    String USER_ID = "userId";
    String USERNAME_PREFIX = "用户";
    Long DEFAULT_ADDITION_METHOD = 1L;
    String DEFAULT_SEX = "男";
    String DEFAULT_AREA = "中国";
    String DEFAULT_PERSONAL_SIGNATURE = "这个人很懒什么都没有留下~";
    DateTimeFormatter DATA_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Integer GROUP_NORMAL_STATUS = 1;
    Integer GROUP_FORBIDDEN_STATUS = 0;

    Integer NOT_FRIEND = 0;
    Integer CONTACT_FRIEND = 1;
    Integer DELETE = 2;
    Integer BE_DELETE = 3;
    Integer BLACKLIST = 4;
    Integer BE_BLACKLIST = 5;

    Integer FRIEND_CONTACT_TYPE = 0;
    Integer GROUP_CONTACT_TYPE = 1;
}
