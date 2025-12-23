package xyz.uz.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.uz.exception.BaseExceptionCode;

/**
 * 系统异常码
 */

@Getter
@AllArgsConstructor
public enum SystemExceptionCode implements BaseExceptionCode {

    /**
     *  该异常的抛出代表该数据本应该存在，但由于某些原因导致数据不一致（前端后端数据不一致）
     */
    UNEXPECTED_STATE("S001","非预期状态"),
    DATA_EXPIRED("S002","数据已过期");

    private final String code;
    private final String desc;
}
