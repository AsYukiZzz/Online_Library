package xyz.uz.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    /**
     * 使用默认错误码 (例如 500 或 自定义业务通用错误码)
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 指定错误码和错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用 ResultCode 构造异常
     */
    public BusinessException(BaseExceptionCode resultCode){
        super(resultCode.getDesc());
        this.code = resultCode.getCode();
    }
}
