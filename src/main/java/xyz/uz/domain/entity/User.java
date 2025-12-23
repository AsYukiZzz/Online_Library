package xyz.uz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.uz.enums.UserPermission;

/**
 * 用户 Entity
 */

@Data
@TableName(value ="user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码 (BCRYPT加密)
     */
    @TableField(value = "password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 权限(0:管理员 1:用户)
     */
    @TableField(value = "permission")
    private UserPermission permission;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 紧急联系人
     */
    @TableField(value = "emergency_contact")
    private String emergencyContact;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 是否存在逾期不还行为
     */
    private Integer isOverdue;
}