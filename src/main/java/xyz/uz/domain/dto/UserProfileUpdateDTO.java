package xyz.uz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 紧急联系人
     */
    private String emergencyContact;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;
}
