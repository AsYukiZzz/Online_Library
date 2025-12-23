package xyz.uz.anno;

import xyz.uz.enums.UserPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限检查（Role）AOP注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {

    /**
     * 允许访问的角色集合
     */
    UserPermission[] value();

    /**
     * 校验模式 （和、或）
     */
    CheckMode mode() default CheckMode.OR;
    enum CheckMode {AND,OR}
}
