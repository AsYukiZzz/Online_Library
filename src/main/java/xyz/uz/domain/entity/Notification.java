package xyz.uz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.uz.enums.ReadingStatus;

/**
 * 通知表
 */

@Data
@TableName(value ="notification")
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {
    /**
     * 接收通知的用户ID (逻辑外键关联user.id)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 通知标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 通知内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 通知类型 (如: 逾期提醒, 审核结果)
     */
    @TableField(value = "type")
    private String type;

    /**
     * 是否已读 (0:未读 1:已读)
     */
    @TableField(value = "is_read")
    private ReadingStatus isRead;
}