package xyz.uz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import xyz.uz.domain.entity.AppointmentRecords;
import xyz.uz.domain.entity.Book;
import xyz.uz.domain.query.AppointmentRecordQuery;
import xyz.uz.domain.vo.AppointmentRecordVO;
import xyz.uz.domain.vo.RenewalRecordVO;
import xyz.uz.enums.AppointmentStatus;
import xyz.uz.exception.BusinessException;
import xyz.uz.exception.code.BusinessExceptionCode;
import xyz.uz.exception.code.SystemExceptionCode;
import xyz.uz.mapper.BookMapper;
import xyz.uz.service.AppointmentRecordsService;
import xyz.uz.mapper.AppointmentRecordsMapper;
import org.springframework.stereotype.Service;

/**
* 针对表【appointment_records(预约记录表)】的数据库操作Service实现
*/
@Service
public class AppointmentRecordsServiceImpl extends ServiceImpl<AppointmentRecordsMapper, AppointmentRecords> implements AppointmentRecordsService{

    @Autowired
    private AppointmentRecordsMapper appointmentRecordsMapper;

    @Autowired
    private BookMapper bookMapper;

    /**
     * 查询预约列表
     */
    @Override
    public IPage<AppointmentRecordVO> pageQuery(AppointmentRecordQuery appointmentRecordQuery) {

        // 1. 设置Id
        long userId = StpUtil.getLoginIdAsLong();
        appointmentRecordQuery.setUserId(userId);

        // 2. 配置分页参数 IPage
        IPage<RenewalRecordVO> infoIPage = new Page<>(appointmentRecordQuery.getPage(), appointmentRecordQuery.getSize());

        // 3. 执行分页查询
        return appointmentRecordsMapper.pageQuery(infoIPage, appointmentRecordQuery);
    }

    /**
     * 预约
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserve(Long userId, Long bookId) {
        if (userId == null || bookId == null) {
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 1. 检查书籍是否存在
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            throw new BusinessException(BusinessExceptionCode.BOOK_NOT_FOUND);
        }

        // 2. 封装数据
        AppointmentRecords newRecord = new AppointmentRecords();
        newRecord.setBookId(bookId);
        newRecord.setUserId(userId);
        newRecord.setStatus(AppointmentStatus.RESERVATION_IN_PROGRESS);

        // 3. 保存
        try {
            appointmentRecordsMapper.insert(newRecord);
        } catch (DataIntegrityViolationException e) {
            // user_id与book_id有联合唯一索引，保证数据唯一性，即一个人对一本书只能预约一次
            throw new BusinessException(BusinessExceptionCode.DUPLICATE_RESERVATION);
        }
    }

    /**
     * 取消预约
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long userId, Long id) {
        AppointmentRecords record = appointmentRecordsMapper.selectById(id);

        // 健壮性校验
        if (record == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        if (!record.getUserId().equals(userId)){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        if (!record.getStatus().equals(AppointmentStatus.RESERVATION_IN_PROGRESS)){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        record.setStatus(AppointmentStatus.CANCEL);

        // 乐观锁
        int updateRows = appointmentRecordsMapper.updateById(record);
        if (updateRows == 0){
            throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
        }
    }
}




