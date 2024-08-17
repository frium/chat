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
    Integer DEFAULT_ADDITION_METHOD = 1;
    String DEFAULT_SEX = "男";
    String DEFAULT_AREA = "中国";
    String DEFAULT_PERSONAL_SIGNATURE = "这个人很懒什么都没有留下~";
    String DEFAULT_AVATAR = "https://blog.frium.top/upload/%E6%B5%81%E8%90%A4.jpg";
    String DEFAULT_ADD_MESSAGE = "我是%s,久仰大名,想申请为你好友";
    DateTimeFormatter DATA_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //用户的状态
    Integer FORBIDDEN_USER=0;
    Integer NORMAL_USER=1;

    //好友添加方式
    Integer NO_APPLY = 0;
    Integer NEED_APPLY = 1;

    //好友申请状态
    Integer BE_REJECT = -1;
    Integer UNTREATED = 0;
    Integer AGREE = 1;


    //群的状态
    Integer GROUP_FORBIDDEN_STATUS = 0;
    Integer GROUP_NORMAL_STATUS = 1;

    //好友之间的关系
    Integer NOT_FRIEND = 0;
    Integer FRIEND = 1;
    Integer DELETE = 2;
    Integer BE_DELETE = 3;
    Integer BLACKLIST = 4;
    Integer BE_BLACKLIST = 5;


    //联系人之间的关系
    Integer FRIEND_CONTACT_TYPE = 0;
    Integer GROUP_CONTACT_TYPE = 1;
}
