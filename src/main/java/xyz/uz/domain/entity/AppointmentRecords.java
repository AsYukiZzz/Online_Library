package xyz.uz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.uz.enums.AppointmentStatus;

/**
 * 预约记录表
 */

@Data
@TableName(value ="appointment_records")
@EqualsAndHashCode(callSuper = true)
public class AppointmentRecords extends BaseEntity {

    /**
     * 预约用户ID (逻辑外键 -> user.id)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 预约书籍ID (逻辑外键 -> book.id)
     */
    @TableField(value = "book_id")
    private Long bookId;

    /**
     * 状态(0:预约中 1:已通知 2:已取消)
     */
    @TableField(value = "status")
    private AppointmentStatus status;
}