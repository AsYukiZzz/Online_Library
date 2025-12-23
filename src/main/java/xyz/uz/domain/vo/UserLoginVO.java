package xyz.uz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.UserPermission;

/**
 * 用户返回信息
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 权限(0:管理员 1:用户)
     */
    private UserPermission permission;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
