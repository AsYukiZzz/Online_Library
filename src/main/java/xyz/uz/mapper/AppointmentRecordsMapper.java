package xyz.uz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.uz.domain.entity.AppointmentRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.uz.domain.query.AppointmentRecordQuery;
import xyz.uz.domain.vo.AppointmentRecordVO;
import xyz.uz.domain.vo.RenewalRecordVO;

/**
* 针对表【appointment_records(预约记录表)】的数据库操作Mapper
*/
public interface AppointmentRecordsMapper extends BaseMapper<AppointmentRecords> {

    /**
     * 预约分页条件查询
     */
    IPage<AppointmentRecordVO> pageQuery(IPage<RenewalRecordVO> infoIPage, AppointmentRecordQuery appointmentRecordQuery);
}




