package xyz.uz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.uz.domain.dto.BorrowApplicationDTO;
import xyz.uz.domain.dto.ReviewResultDTO;
import xyz.uz.domain.entity.BorrowRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.uz.domain.query.BorrowRecordQuery;
import xyz.uz.domain.vo.BorrowRecordVO;

/**
* 针对表【borrow_records(借阅记录表)】的数据库操作Service
*/
public interface BorrowRecordsService extends IService<BorrowRecords> {

    /**
     * 查询借阅列表（用户）
     */
    IPage<BorrowRecordVO> pageQueryUser(BorrowRecordQuery borrowRecordQuery);

    /**
     * 用户查询借阅列表（管理员）
     */
    IPage<BorrowRecordVO> pageQueryAdmin(BorrowRecordQuery borrowRecordQuery);

    /**
     * 借阅记录查询（根据借阅记录 ID）
     */
    BorrowRecordVO getInfoById(Long id);

    /**
     * 发出借阅申请
     */
    void borrow(BorrowApplicationDTO borrowApplicationDTO);

    /**
     * 取书确认
     */
    void pickupConfirm(Long id);

    /**
     * 还书确认
     */
    void returnConfirm(Long id);

    /**
     * 借阅申请处理
     */
    void applicantProcess(ReviewResultDTO reviewResultDTO);
}
