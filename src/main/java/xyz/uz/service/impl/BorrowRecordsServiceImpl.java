package xyz.uz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.uz.constant.NotificationContentConstant;
import xyz.uz.domain.dto.BorrowApplicationDTO;
import xyz.uz.domain.dto.ReviewResultDTO;
import xyz.uz.domain.entity.Book;
import xyz.uz.domain.entity.BorrowRecords;
import xyz.uz.domain.query.BorrowRecordQuery;
import xyz.uz.domain.vo.BorrowRecordVO;
import xyz.uz.domain.vo.RenewalRecordVO;
import xyz.uz.domain.vo.WebsocketMessageVO;
import xyz.uz.enums.BorrowStatus;
import xyz.uz.event.BookArrivalEvent;
import xyz.uz.exception.BusinessException;
import xyz.uz.exception.code.BusinessExceptionCode;
import xyz.uz.exception.code.SystemExceptionCode;
import xyz.uz.mapper.BookMapper;
import xyz.uz.mapper.BorrowRecordsMapper;
import xyz.uz.mapper.RenewalRecordsMapper;
import xyz.uz.service.BorrowRecordsService;
import xyz.uz.service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 针对表【borrow_records(借阅记录表)】的数据库操作Service实现
 */
@Service
public class BorrowRecordsServiceImpl extends ServiceImpl<BorrowRecordsMapper, BorrowRecords> implements BorrowRecordsService {

    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;

    @Autowired
    private RenewalRecordsMapper renewalRecordsMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 查询借阅列表（用户端）
     */
    @Override
    public IPage<BorrowRecordVO> pageQueryUser(BorrowRecordQuery borrowRecordQuery) {

        // 配置学生 ID
        borrowRecordQuery.setStudentId(StpUtil.getLoginIdAsLong());

        // 配置分页参数 IPage
        IPage<BorrowRecordVO> infoIPage = new Page<>(borrowRecordQuery.getPage(), borrowRecordQuery.getSize());

        // 执行分页查询
        return borrowRecordsMapper.pageQuery(infoIPage, borrowRecordQuery);
    }

    /**
     * 查询借阅列表（管理员）
     */
    @Override
    public IPage<BorrowRecordVO> pageQueryAdmin(BorrowRecordQuery borrowRecordQuery) {

        // 配置分页参数 IPage
        IPage<BorrowRecordVO> infoIPage = new Page<>(borrowRecordQuery.getPage(), borrowRecordQuery.getSize());

        // 执行分页查询
        return borrowRecordsMapper.pageQuery(infoIPage, borrowRecordQuery);
    }

    /**
     * 借阅记录查询（根据借阅记录 ID）
     */
    @Override
    public BorrowRecordVO getInfoById(Long id) {
        // 根据 id 查询书籍
        BorrowRecordVO borrowRecordVO = borrowRecordsMapper.getInfoById(id);

        // 确保 id 存在
        if (borrowRecordVO == null) {
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 查询续借记录信息并封装
        List<RenewalRecordVO> renewalRecords = renewalRecordsMapper.getRenewalRecordsByBorrowRecordId(borrowRecordVO.getId());
        borrowRecordVO.setRenewalRecordList(renewalRecords);

        return borrowRecordVO;
    }

    /**
     * 发出借阅申请
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void borrow(BorrowApplicationDTO borrowApplicationDTO) {
        // 1. 尝试扣减库存（数据库原子操作）
        int affectRows = bookMapper.deductionInventory(borrowApplicationDTO.getBookId(), borrowApplicationDTO.getBorrowAmount());

        // 2. 库存扣减失败
        if (affectRows == 0) {
            // 2.1 查询传入的书籍 ID 是否存在
            Book book = bookMapper.selectById(borrowApplicationDTO.getBookId());

            // 2.1.1 书籍不存在，并非使用者造成
            if (book == null) {
                throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
            }

            // 2.1.2 书籍库存不足
            throw new BusinessException(BusinessExceptionCode.INSUFFICIENT_STOCK);
        }

        // 2. 构造借阅记录
        // 2.1 填充借阅记录基本信息
        BorrowRecords borrowRecords = new BorrowRecords();
        borrowRecords.setBookId(borrowApplicationDTO.getBookId());
        borrowRecords.setStudentId(StpUtil.getLoginIdAsLong());
        borrowRecords.setBorrowAmount(borrowApplicationDTO.getBorrowAmount());
        borrowRecords.setLoanDuration(borrowApplicationDTO.getLoanDuration());
        borrowRecords.setStatus(BorrowStatus.PENDING_APPROVAL);

        // 2.2 属性复制（BorrowApplicationDTO -> BorrowRecords）
        BeanUtils.copyProperties(borrowApplicationDTO, borrowRecords);

        // 3. 插入数据库
        borrowRecordsMapper.insert(borrowRecords);

        try {
            // 4. 向在线管理员推送通知
            notificationService.broadcastToAdmin(
                    WebsocketMessageVO.standardNotification(NotificationContentConstant.BORROWING_APPLICATION_PENDING)
            );
        } catch (Exception e) {
            log.error("向全体管理广播失败，{}", e);
        }
    }

    /**
     * 借阅申请处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applicantProcess(ReviewResultDTO reviewResultDTO) {

        // TODO 待重构
        // 1. 查询借阅记录，并进行非空校验
        BorrowRecords record = borrowRecordsMapper.selectById(reviewResultDTO.getId());
        if (record == null) {
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 2. 业务校验（状态应为待审核）
        if (!record.getStatus().equals(BorrowStatus.PENDING_APPROVAL)) {
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 3. 新审核状态判断
        LocalDateTime now = LocalDateTime.now();
        BorrowStatus newStatus;
        switch (reviewResultDTO.getReviewResult()) {
            case APPROVED:
                newStatus = BorrowStatus.READY_FOR_PICKUP;
                record.setDueTime(now.plusDays(record.getLoanDuration()));
                break;
            case REJECTED:
                newStatus = BorrowStatus.REJECTED;

                // 1. 归还库存
                bookMapper.increaseInventory(record.getBookId(), record.getBorrowAmount());

                // 2. 发布事件，告知用户补货
                BookArrivalEvent bookArrivalEvent = new BookArrivalEvent(this, record.getBookId());
                applicationEventPublisher.publishEvent(bookArrivalEvent);

                break;
            default:
                throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }


        // 4. 信息填装
        record.setReviewTime(now);
        record.setReviewerId(StpUtil.getLoginIdAsLong());
        record.setReviewComment(reviewResultDTO.getReviewComment());
        record.setStatus(newStatus);

        // 5. 数据更新（乐观锁）
        int updateRows = borrowRecordsMapper.updateById(record);
        if (updateRows == 0) {
            throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
        }

        // 6. 对用户进行通知
        notificationService.sendToUser(
                record.getStudentId(),
                WebsocketMessageVO.standardNotification(NotificationContentConstant.APPLICATION_PROCESSED)
        );
    }

    /**
     * 取书确认
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pickupConfirm(Long id) {
        // 1. 查询借阅记录，并进行非空校验
        BorrowRecords borrowRecord = borrowRecordsMapper.selectById(id);
        if (borrowRecord == null) {
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 2. 切换借阅记录状态
        int updateRows = borrowRecordsMapper.updateStatus(borrowRecord.getVersion(), BorrowStatus.LOANED, BorrowStatus.READY_FOR_PICKUP);
        if (updateRows == 0) {
            BorrowRecords temp = borrowRecordsMapper.selectById(id);

            // 2.1 未预期的异常
            if (!temp.getStatus().equals(BorrowStatus.READY_FOR_PICKUP)) {
                throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
            }

            // 2.2 数据已过期（乐观锁校验失败）
            throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
        }
    }

    /**
     * 还书确认
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnConfirm(Long id) {
        // 1. 查询借阅记录，并进行非空校验
        BorrowRecords borrowRecord = borrowRecordsMapper.selectById(id);
        if (borrowRecord == null) {
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 2. 状态检验与切换（原子SQL、乐观锁）
        int updateRows = borrowRecordsMapper.updateStatus(
                borrowRecord.getVersion(),
                BorrowStatus.RETURNED,
                BorrowStatus.LOANED, BorrowStatus.OVERDUE
        );

        if (updateRows == 0) {
            BorrowRecords temp = borrowRecordsMapper.selectById(id);

            // 2.1 未预期的异常
            if (!temp.getStatus().equals(BorrowStatus.LOANED) && !temp.getStatus().equals(BorrowStatus.OVERDUE)) {
                throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
            }

            // 2.2 数据已过期（乐观锁校验失败）
            throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
        }

        // 3. 归还库存
        bookMapper.increaseInventory(borrowRecord.getBookId(), borrowRecord.getBorrowAmount());

        try {
            // 4. 创建事件，通知预约用户到货
            BookArrivalEvent bookArrivalEvent = new BookArrivalEvent(this, borrowRecord.getBookId());
            applicationEventPublisher.publishEvent(bookArrivalEvent);
        } catch (RuntimeException e) {
            log.error("通知预约用户失败", e);
        }
    }
}




