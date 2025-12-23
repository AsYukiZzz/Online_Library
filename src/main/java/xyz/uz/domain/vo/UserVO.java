package xyz.uz.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 真实姓名
     */
    @TableField(value = "name")
    private String name;

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
}
