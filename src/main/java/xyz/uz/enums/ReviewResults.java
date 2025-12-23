package xyz.uz.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核结果封装
 */

@Getter
@AllArgsConstructor
public enum ReviewResults implements BaseEnum{

    APPROVED(0,"通过"),
    REJECTED(1,"驳回");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;
}
