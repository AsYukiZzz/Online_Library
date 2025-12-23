package xyz.uz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.BorrowStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordVO {

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 借阅学生姓名
     */
    private String studentName;

    /**
     * 书名
     */
    private String bookName;

    /**
     * 申请数量
     */
    private Integer borrowAmount;

    /**
     * 借阅时长(天)
     */
    private Integer loanDuration;

    /**
     * 状态(0:审核中 1:待取书 2:已借出 3:已归还 4:已逾期 5:已驳回)
     */
    private BorrowStatus status;

    /**
     * 申请日期
     */
    private LocalDateTime createTime;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核意见
     */
    private String reviewComment;

    /**
     * 应归还时间
     */
    private LocalDateTime dueTime;

    /**
     * 续借记录封装
     */
    private List<RenewalRecordVO> renewalRecordList;
}
