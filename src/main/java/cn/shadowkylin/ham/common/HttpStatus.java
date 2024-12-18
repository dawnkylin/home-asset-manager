package cn.shadowkylin.ham.common;

/**
 * @创建人 li cong
 * @创建时间 2023/3/28
 * @描述 状态码
 */
public class HttpStatus {
    //手机号已经注册
    public static final int PHONE_EXIST = 1001;
    //未登录
    public static final int NOT_LOGIN = 1002;
    //登录过期
    public static final int LOGIN_EXPIRE = 1003;
    // 验证码过期
    public static final int CODE_EXPIRED = 1004;
    // 验证码错误
    public static final int CODE_ERROR = 1005;
    public static final int PHONE_OR_PASSWORD_ERROR = 1006;
    // 手机号不存在
    public static final int PHONE_NOT_EXIST = 1007;
    // 资产序列号不存在
    public static final int ASSET_SERIAL_NUMBER_NOT_EXIST = 1008;
    //无权限UNAUTHORIZED
    public static final int UNAUTHORIZED = 1009;
    //家庭不存在
    public static final int HOME_NOT_EXIST = 1010;
    //违法操作
    public static final int ILLEGAL_OPERATION = 1011;
    public static final Object PASSWORD_ERROR = 1012;
    public static final Object PASSWORD_SAME = 1013;
}
