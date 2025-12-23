package xyz.uz.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReadingStatus implements BaseEnum{

    UNREAD(0,"未读"),
    READ(1,"已读");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;
}
