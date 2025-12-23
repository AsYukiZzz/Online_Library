package xyz.uz.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.RenewalStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenewalRecordQuery {

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
     * 关联的借阅记录ID (逻辑外键 -> borrow_records.id)
     */
    private Long borrowRecordId;

    /**
     * 续借学生ID (逻辑外键 -> user.id)
     */
    private Long studentId;

    /**
     * 状态(0:审核中 1:续借成功 2:续借失败)
     */
    private RenewalStatus status;

    /**
     * 审核人ID (逻辑外键 -> user.id)
     */
    private Long reviewerId;
}
