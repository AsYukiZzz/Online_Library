package xyz.uz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.ReviewResults;

/**
 * 请求审核结果 DTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResultDTO {

    /**
     * 审核单 ID
     */
    private Long id;

    /**
     * 审核结果（通过、拒绝）
     */
    private ReviewResults reviewResult;

    /**
     * 审核意见
     */
    private String reviewComment;
}
