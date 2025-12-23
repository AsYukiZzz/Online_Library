package xyz.uz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 借阅记录 DTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowApplicationDTO {

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 借阅图书ID (逻辑外键, 关联book.id)
     */
    private Long bookId;

    /**
     * 申请数量
     */
    private Integer borrowAmount;

    /**
     * 借阅时长(天)
     */
    private Integer loanDuration;
}
