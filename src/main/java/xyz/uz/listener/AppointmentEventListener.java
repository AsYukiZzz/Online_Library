package xyz.uz.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import xyz.uz.constant.NotificationContentConstant;
import xyz.uz.domain.entity.AppointmentRecords;
import xyz.uz.domain.vo.WebsocketMessageVO;
import xyz.uz.enums.AppointmentStatus;
import xyz.uz.event.BookArrivalEvent;
import xyz.uz.event.BookRemovedEvent;
import xyz.uz.mapper.AppointmentRecordsMapper;
import xyz.uz.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AppointmentEventListener {

    @Autowired
    private AppointmentRecordsMapper appointmentRecordsMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 通知预约用户
     */
    @Async
    @TransactionalEventListener
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void onBookArrival(BookArrivalEvent event) {
        // 获取要进行通知的列表
        List<AppointmentRecords> records = appointmentRecordsMapper.selectList(
                new QueryWrapper<AppointmentRecords>()
                        .eq("book_id", event.getBookId())
                        .eq("status", AppointmentStatus.RESERVATION_IN_PROGRESS)
        );

        if (records == null || records.isEmpty()) {
            return;
        }

        // TODO 通知插入可以改成批量处理
        ArrayList<Long> ids = new ArrayList<>();
        for (AppointmentRecords record : records) {
            try {
                // 1. 发送通知给用户
                notificationService.sendToUser(record.getUserId(), WebsocketMessageVO.standardNotification(NotificationContentConstant.APPOINTMENT_COMPLETED));

                // 2. 收集Id
                ids.add(record.getId());

            } catch (Exception e) {
                log.error("向用户id={}发送通知失败，对应书籍id={}", record.getUserId(), event.getBookId(), e);
            }
        }

        // 批量写回
        appointmentRecordsMapper.update(null,
                new LambdaUpdateWrapper<AppointmentRecords>()
                        .in(AppointmentRecords::getId, ids)
                        .set(AppointmentRecords::getStatus, AppointmentStatus.NOTIFIED)
        );
    }

    @EventListener
    public void onBookRemoved(BookRemovedEvent event) {
        // TODO 书籍下架预约自动取消逻辑，日后再做
    }
}
