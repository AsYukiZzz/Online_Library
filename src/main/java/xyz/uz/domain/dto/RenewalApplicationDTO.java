package xyz.uz.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 续集记录 DTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenewalApplicationDTO {

    /**
     * 关联的借阅记录ID (逻辑外键关联borrow_records.id)
     */
    @TableField(value = "borrow_record_id")
    private Long borrowRecordId;

    /**
     * 续借时长(天)
     */
    @TableField(value = "renew_duration")
    private Integer renewDuration;
}
