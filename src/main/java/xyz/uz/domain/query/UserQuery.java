package xyz.uz.domain.query;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.UserPermission;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery {

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 单页条目数
     */
    private Integer size;

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
     * 权限(0:管理员 1:用户)
     */
    @TableField(value = "permission")
    private UserPermission permission;

    /**
     * 是否存在逾期不还行为
     */
    private Integer isOverdue;
}
