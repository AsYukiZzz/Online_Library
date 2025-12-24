package xyz.uz.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.uz.exception.BaseExceptionCode;

/**
 * 业务异常码
 */

@Getter
@AllArgsConstructor
public enum BusinessExceptionCode implements BaseExceptionCode {

    // 用户部分业务异常码（并非认证部分）
    USER_ALREADY_EXISTS(21001,"用户名已存在"),

    // 书籍部分业务相关异常码
    BOOK_ALREADY_EXISTS(22001,"书籍已存在"),
    BOOK_NOT_FOUND(22002,"书籍不存在"),
    INSUFFICIENT_STOCK(22003,"库存不足"),

    // 借阅部分业务相关异常码


    // 续借部分业务相关异常码
    RENEWAL_REQUEST_IN_PROGRESS(24001,"续借请求正在处理中，不允许重复请求"),

    // 预约部分业务相关异常码
    DUPLICATE_RESERVATION(25001,"不允许对同一本书重复预约");

    private final Integer code;
    private final String desc;
}
