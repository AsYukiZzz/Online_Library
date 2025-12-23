package xyz.uz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.uz.constant.NotificationContentConstant;
import xyz.uz.domain.dto.RenewalApplicationDTO;
import xyz.uz.domain.dto.ReviewResultDTO;
import xyz.uz.domain.entity.BorrowRecords;
import xyz.uz.domain.entity.RenewalRecords;
import xyz.uz.domain.query.RenewalRecordQuery;
import xyz.uz.domain.vo.RenewalRecordVO;
import xyz.uz.domain.vo.WebsocketMessageVO;
import xyz.uz.enums.RenewalStatus;
import xyz.uz.exception.BusinessException;
import xyz.uz.exception.code.BusinessExceptionCode;
import xyz.uz.exception.code.SystemExceptionCode;
import xyz.uz.mapper.BorrowRecordsMapper;
import xyz.uz.service.NotificationService;
import xyz.uz.service.RenewalRecordsService;
import xyz.uz.mapper.RenewalRecordsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* 针对表【renewal_records(续借记录表)】的数据库操作Service实现
*/
@Service
public class RenewalRecordsServiceImpl extends ServiceImpl<RenewalRecordsMapper, RenewalRecords> implements RenewalRecordsService{

    @Autowired
    private RenewalRecordsMapper renewalRecordsMapper;

    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 查看续借请求详细信息（根据ID）
     */
    @Override
    public RenewalRecordVO getInfoById(Long id) {
        // 根据 id 查询书籍
        RenewalRecords renewalRecord = renewalRecordsMapper.selectById(id);

        // 确保 ID 存在
        if (renewalRecord == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 封装到 RenewalRecordVO
        RenewalRecordVO renewalRecordVO = new RenewalRecordVO();
        BeanUtils.copyProperties(renewalRecord,renewalRecordVO);

        return renewalRecordVO;
    }

    /**
     * 查询续借申请列表（只能管理员查）
     */
    @Override
    public IPage<RenewalRecordVO> pageQueryAdmin(RenewalRecordQuery renewalRecordQuery) {

        // 配置分页参数 IPage
        IPage<RenewalRecordVO> infoIPage = new Page<>(renewalRecordQuery.getPage(), renewalRecordQuery.getSize());

        // 执行分页查询
        return renewalRecordsMapper.pageQuery(infoIPage, renewalRecordQuery);
    }

    /**
     * 发出续借申请
     */
    @Override
    public void renew(RenewalApplicationDTO renewalApplicationDTO) {
        // 1. 检查是否已经状态为审核中的续借请求，若存在则驳回本次请求
        Long renewalRequestCount = renewalRecordsMapper.selectCount(
                new QueryWrapper<RenewalRecords>()
                        .eq("borrow_record_id", renewalApplicationDTO.getBorrowRecordId())
                        .eq("status", RenewalStatus.PENDING_APPROVAL)
        );
        if (renewalRequestCount > 0){
            throw new BusinessException(BusinessExceptionCode.RENEWAL_REQUEST_IN_PROGRESS);
        }

        // 构造 PO
        RenewalRecords renewalRecord = new RenewalRecords();
        renewalRecord.setStudentId(StpUtil.getLoginIdAsLong());
        renewalRecord.setStatus(RenewalStatus.PENDING_APPROVAL);
        BeanUtils.copyProperties(renewalApplicationDTO,renewalRecord);

        // 插入数据库
        renewalRecordsMapper.insert(renewalRecord);

        // 向全体管理员广播通知
        try{
            notificationService.broadcastToAdmin(
                    WebsocketMessageVO.standardNotification(NotificationContentConstant.RENEWAL_APPLICATION_PENDING)
            );
        } catch (Exception e) {
            log.error("向全体管理广播失败，{}", e);
        }
    }

    /**
     * 续借申请处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applicantProcess(ReviewResultDTO reviewResultDTO) {
        // todo 查看
        // 1. 查询续借申请，并验证是否存在
        RenewalRecords record = renewalRecordsMapper.selectById(reviewResultDTO.getId());
        if (record == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 2. 状态检查，状态需要是审核中
        if (!record.getStatus().equals(RenewalStatus.PENDING_APPROVAL)){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 3. 新状态判断
        RenewalStatus newStatus;
        switch (reviewResultDTO.getReviewResult()){
            case APPROVED:
                // 3.1.1 更新状态
                newStatus = RenewalStatus.ACCEPT;

                // 3.1.2 更新借阅记录的约定归还日期
                BorrowRecords borrowRecord = borrowRecordsMapper.selectById(record.getBorrowRecordId());
                borrowRecord.setDueTime(borrowRecord.getDueTime().plusDays(record.getRenewDuration()));
                int updateRows = borrowRecordsMapper.updateById(borrowRecord);
                if (updateRows == 0){
                    throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
                }
                break;
            case REJECTED:
                newStatus = RenewalStatus.REJECTED;
                break;
            default:
                throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 4. 信息填装
        record.setStatus(newStatus);
        record.setReviewTime(LocalDateTime.now());
        record.setReviewerId(StpUtil.getLoginIdAsLong());
        record.setReviewComment(reviewResultDTO.getReviewComment());

        // 5. 持久化
        int updateRows = renewalRecordsMapper.updateById(record);
        if (updateRows == 0){
            throw new BusinessException(SystemExceptionCode.DATA_EXPIRED);
        }

        // 6. 向用户发送通知
        notificationService.sendToUser(
                record.getStudentId(),
                WebsocketMessageVO.standardNotification(NotificationContentConstant.APPLICATION_PROCESSED)
        );
    }
}




