package xyz.uz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.uz.domain.entity.RenewalRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.uz.domain.query.RenewalRecordQuery;
import xyz.uz.domain.vo.RenewalRecordVO;

import java.util.List;

/**
* 针对表【renewal_records(续借记录表)】的数据库操作Mapper
*/

public interface RenewalRecordsMapper extends BaseMapper<RenewalRecords> {

    /**
     * 根据借阅记录 ID 查询 续借记录
     */
    @Select("select * from renewal_records where borrow_record_id = #{id}")
    List<RenewalRecordVO> getRenewalRecordsByBorrowRecordId(@Param("id") Long id);

    /**
     * 续借列表分页条件查询
     */
    IPage<RenewalRecordVO> pageQuery(IPage<RenewalRecordVO> infoIPage, RenewalRecordQuery renewalRecordQuery);
}




