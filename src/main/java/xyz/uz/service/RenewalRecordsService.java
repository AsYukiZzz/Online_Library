package xyz.uz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import xyz.uz.domain.dto.RenewalApplicationDTO;
import xyz.uz.domain.dto.ReviewResultDTO;
import xyz.uz.domain.entity.RenewalRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.uz.domain.query.RenewalRecordQuery;
import xyz.uz.domain.vo.RenewalRecordVO;

/**
* 针对表【renewal_records(续借记录表)】的数据库操作Service
*/
public interface RenewalRecordsService extends IService<RenewalRecords> {

    /**
     * 查看续借请求详细信息（根据ID）
     */
    RenewalRecordVO getInfoById(Long id);

    /**
     * 查询续借申请列表（只能管理员查）
     */
    IPage<RenewalRecordVO> pageQueryAdmin(RenewalRecordQuery renewalRecordQuery);

    /**
     * 发出续借申请
     */
    void renew(RenewalApplicationDTO renewalApplicationDTO);

    /**
     * 续借申请处理
     */
    void applicantProcess(ReviewResultDTO reviewResultDTO);
}
