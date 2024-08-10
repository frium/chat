package top.frium.common;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    SUCCESS(200,"操作成功",200),
    FAIL(-1, "操作失败",200),
    NOT_FOUND(4040, "未找到相关内容",200),
    VALUE_ERROR(4041,"参数有误",400),
    USER_EXIST(1001,"账号已被注册",200),
    LOGIN_FAIL(1002,"登录失败,请检查账号或者密码是否有误",200),
    NOT_LOGIN(1003,"用户未登陆",401),
    ERROR_VERIFY(1004,"验证码错误",200),
    USER_NOT_EXIST(1004, "用户不存在",200),
    PERSONAL_ID_ERROR(1005,"身份证格式有误",200),
    PHONE_ERROR(1006,"手机号格式有误",200),
    PASSWORD_ERROR(1007,"密码格式有误",200),
    NO_PERMISSION(1008,"无权限访问",403),
    BE_BLACKLIST(1009,"对方以将你拉黑,无法添加好友",200),
    IS_FRIEND(1010,"您和对方已经是好友了,无需继续添加",200),
    IS_APPLY(1011,"您已经发送过好友申请了,请等候对方的回复",200);

    private final Integer code;
    private final String desc;
    private final Integer httpStatusCode;
}
