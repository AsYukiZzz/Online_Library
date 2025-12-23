package xyz.uz.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.RenewalStatus;

import java.time.LocalDateTime;

/**
 * 续借记录 VO
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalRecordVO {

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 关联的借阅记录ID (逻辑外键 -> borrow_records.id)
     */
    private Long borrowRecordId;

    /**
     * 书名
     */
    private String bookName;

    /**
     * 续借学生ID (逻辑外键 -> user.id)
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 续借时长(天)
     */
    private Integer renewDuration;

    /**
     * 状态(0:审核中 1:续借成功 2:续借失败)
     */
    private RenewalStatus status;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 审核人ID (逻辑外键 -> user.id)
     */
    private Long reviewerId;

    /**
     * 审核备注
     */
    private String reviewComment;

    /**
     * 申请时间（创建时间）
     */
    private LocalDateTime createTime;
}
