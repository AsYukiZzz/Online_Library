package xyz.uz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍数据模型
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码 (实际应用中应存储哈希值)
     */
    private String password;

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

    /**
     * 是否存在逾期不还行为
     */
    private Integer isOverdue;
}
