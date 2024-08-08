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
}
