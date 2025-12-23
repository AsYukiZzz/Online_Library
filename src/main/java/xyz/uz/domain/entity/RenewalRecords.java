package xyz.uz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.uz.enums.RenewalStatus;

/**
 * 续借记录表
 */

@Data
@TableName(value ="renewal_records")
@EqualsAndHashCode(callSuper = true)
public class RenewalRecords extends BaseEntity {

    /**
     * 关联的借阅记录ID (逻辑外键 -> borrow_records.id)
     */
    @TableField(value = "borrow_record_id")
    private Long borrowRecordId;

    /**
     * 续借学生ID (逻辑外键 -> user.id)
     */
    @TableField(value = "student_id")
    private Long studentId;

    /**
     * 续借时长(天)
     */
    @TableField(value = "renew_duration")
    private Integer renewDuration;

    /**
     * 状态(0:审核中 1:续借成功 2:续借失败)
     */
    @TableField(value = "status")
    private RenewalStatus status;

    /**
     * 审核时间
     */
    @TableField(value = "review_time")
    private LocalDateTime reviewTime;

    /**
     * 审核人ID (逻辑外键 -> user.id)
     */
    @TableField(value = "reviewer_id")
    private Long reviewerId;

    /**
     * 审核备注
     */
    @TableField(value = "review_comment")
    private String reviewComment;
}