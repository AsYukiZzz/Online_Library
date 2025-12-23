package xyz.uz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import lombok.*;
import xyz.uz.enums.BorrowStatus;

/**
 * 借阅记录 Entity
 */

@Data
@TableName(value ="borrow_records")
@EqualsAndHashCode(callSuper = true)
public class BorrowRecords extends BaseEntity {

    /**
     * 借阅学生ID (逻辑外键, 关联user.id)
     */
    @TableField(value = "student_id")
    private Long studentId;

    /**
     * 借阅图书ID (逻辑外键, 关联book.id)
     */
    @TableField(value = "book_id")
    private Long bookId;

    /**
     * 申请数量
     */
    @TableField(value = "borrow_amount")
    private Integer borrowAmount;

    /**
     * 借阅时长(天)
     */
    @TableField(value = "loan_duration")
    private Integer loanDuration;

    /**
     * 状态(0:审核中 1:待取书 2:已借出 3:已归还 4:逾期)
     */
    @TableField(value = "status")
    private BorrowStatus status;

    /**
     * 审核时间
     */
    @TableField(value = "review_time")
    private LocalDateTime reviewTime;

    /**
     * 审核人ID (逻辑外键, 关联user.id)
     */
    @TableField(value = "reviewer_id")
    private Long reviewerId;

    /**
     * 审核意见
     */
    @TableField(value = "review_comment")
    private String reviewComment;

    /**
     * 应归还时间
     */
    @TableField(value = "due_time")
    private LocalDateTime dueTime;
}