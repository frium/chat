package top.frium.common;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    SUCCESS(200, "操作成功", 200),
    ERROR(-1234, "系统异常", 500),
    FAIL(-1, "操作失败", 200),
    NOT_FOUND(4040, "未找到相关内容", 200),
    VALUE_ERROR(4041, "参数有误", 400),
    USER_EXIST(1001, "账号已被注册", 200),
    LOGIN_FAIL(1002, "登录失败,请检查账号或者密码是否有误", 200),
    NOT_LOGIN(1003, "用户未登陆", 401),
    ERROR_VERIFY(1004, "验证码错误", 200),
    USER_NOT_EXIST(1005, "用户不存在", 200),
    USER_BE_FORBID(1005, "用户被禁用", 200),
    NO_PERMISSION(1008, "无权限访问", 403),
    BE_BLACKLIST(1009, "对方以将你拉黑,无法添加好友", 200),
    IS_FRIEND(1010, "您和对方已经是好友了,无需继续添加", 200),
    IS_APPLY(1011, "您已经发送过好友申请了,请等候对方的回复", 200),
    ID_EXIST(1012, "这个账号太火热啦,已经被捷足先登了,请换一个新的吧~", 200),
    NOT_TIME(1013, "距离您上次更换个人id未满一年,请到时间后再进行更换", 200),
    IS_HAVE(1014, "这个id已经是您的了,不需要更改哦~", 200),
    MYSELF(1015, "自己就是自己的好朋友~", 200),
    DELETED(1016, "您已经将对方删除了,无需继续删除", 200),
    BLACKED_OUT(1017, "您已经将对方拉黑了,无需继续拉黑", 200),
    CONTENT_TOO_LONG(1018,"发送的消息内容太长啦",200),
    FILE_TOO_BIG(1019,"发送的文件大于10M",200),
    NO_FRIEND(1020,"您和对方暂时还不是朋友",200);


    private final Integer code;
    private final String desc;
    private final Integer httpStatusCode;
}
