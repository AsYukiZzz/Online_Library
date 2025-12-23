package xyz.uz.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.BorrowStatus;

import java.time.LocalDateTime;

/**
 * 借阅记录分页查询数据模型
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordQuery {

    /**
     * 页码
     */
    private Integer page;

    /**
     * 单页条目数
     */
    private Integer size;

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 学生 ID
     */
    private Long studentId;

    /**
     * 借阅图书ID (逻辑外键, 关联book.id)
     */
    private String name;

    /**
     * 状态(0:审核中 1:待取书 2:已借出 3:已归还 4:逾期)
     */
    private BorrowStatus status;

    /**
     * 应归还时间
     */
    private LocalDateTime dueTime;

    /**
     * 排序规则
     */
    private String orders;
}
