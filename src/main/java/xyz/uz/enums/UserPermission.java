package xyz.uz.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户权限枚举
 */

@Getter
@AllArgsConstructor
public enum UserPermission implements BaseEnum{

    ADMIN(0,"ADMIN","管理员"),
    USER(1,"USER","普通用户");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String roleName;
    private final String desc;
}
