package xyz.uz.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RenewalStatus implements BaseEnum{

    PENDING_APPROVAL(0,"审核中"),
    ACCEPT(1,"续借请求通过"),
    REJECTED(2,"续借申请被拒绝");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;
}
