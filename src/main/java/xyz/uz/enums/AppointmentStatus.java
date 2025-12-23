package xyz.uz.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentStatus implements BaseEnum{

    RESERVATION_IN_PROGRESS(0,"预约中"),
    NOTIFIED(1,"已通知"),
    CANCEL(2,"已取消");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;
}

