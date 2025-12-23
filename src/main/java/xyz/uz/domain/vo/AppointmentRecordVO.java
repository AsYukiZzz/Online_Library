package xyz.uz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.AppointmentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRecordVO {

    /**
     * 主键 Id
     */
    private Long id;

    /**
     * 预约用户名称
     */
    private String userName;

    /**
     * 预约书籍名称
     */
    private String bookName;

    /**
     * 状态(0:预约中 1:已通知 2:已取消)
     */
    private AppointmentStatus status;
}
