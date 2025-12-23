package xyz.uz.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.uz.exception.BaseExceptionCode;

/**
 * 认证异常码
 */

@Getter
@AllArgsConstructor
public enum AuthExceptionCode implements BaseExceptionCode {

    USER_NOT_FOUND("A001","用户不存在"),
    INVALID_PASSWORD("A002","用户名与密码不匹配");

    private final String code;
    private final String desc;
}
