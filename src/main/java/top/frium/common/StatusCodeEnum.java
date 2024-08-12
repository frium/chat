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
    USER_NOT_EXIST(1004, "用户不存在", 200),
    PERSONAL_ID_ERROR(1005, "身份证格式有误", 200),
    PHONE_ERROR(1006, "手机号格式有误", 200),
    PASSWORD_ERROR(1007, "密码格式有误", 200),
    NO_PERMISSION(1008, "无权限访问", 403),
    BE_BLACKLIST(1009, "对方以将你拉黑,无法添加好友", 200),
    IS_FRIEND(1010, "您和对方已经是好友了,无需继续添加", 200),
    IS_APPLY(1011, "您已经发送过好友申请了,请等候对方的回复", 200),
    ID_EXIST(1012, "这个账号太火热啦,已经被捷足先登了,请换一个新的吧~", 200),
    NOT_TIME(1013, "距离您上次更换个人id未满一年,请到时间后再进行更换", 200),
    IS_HAVE(1014, "这个id已经是您的了,不需要更改哦~", 200),
    ADD_MYSELF(1015, "自己就是自己的好朋友~", 200);


    private final Integer code;
    private final String desc;
    private final Integer httpStatusCode;
}
