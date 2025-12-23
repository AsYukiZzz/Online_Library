package xyz.uz.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.uz.enums.AppointmentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRecordQuery {

    /**
     * 页码
     */
    private Integer page;

    /**
     * 单页条目数
     */
    private Integer size;

    /**
     * 预约用户ID (逻辑外键 -> user.id)
     */
    private Long userId;

    /**
     * 预约书籍ID (逻辑外键 -> book.id)
     */
    private Long bookId;

    /**
     * 状态(0:预约中 1:已通知 2:已取消)
     */
    private AppointmentStatus status;
}
