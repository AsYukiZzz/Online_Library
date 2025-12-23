package xyz.uz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.uz.domain.entity.AppointmentRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.uz.domain.query.AppointmentRecordQuery;
import xyz.uz.domain.vo.AppointmentRecordVO;

/**
* 针对表【appointment_records(预约记录表)】的数据库操作Service
*/
public interface AppointmentRecordsService extends IService<AppointmentRecords> {

    /**
     * 查询预约列表
     */
    IPage<AppointmentRecordVO> pageQuery(AppointmentRecordQuery appointmentRecordQuery);

    /**
     * 预约
     */
    void reserve(Long userId, Long bookId);

    /**
     * 取消预约
     */
    void cancel(Long userId, Long id);
}
