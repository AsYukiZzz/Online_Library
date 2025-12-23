package xyz.uz.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 借书状态枚举
 */

@Getter
@AllArgsConstructor
public enum BorrowStatus implements BaseEnum{

    PENDING_APPROVAL(0,"审核中"),
    READY_FOR_PICKUP(1,"待取书"),
    LOANED(2,"已取书"),
    RETURNED(3,"已归还"),
    OVERDUE(4,"已逾期"),
    REJECTED(5,"被驳回");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;
}
