package xyz.uz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import xyz.uz.domain.entity.BorrowRecords;
import xyz.uz.domain.query.BorrowRecordQuery;
import xyz.uz.domain.vo.BorrowRecordVO;
import xyz.uz.enums.BorrowStatus;

import java.util.List;
import java.util.Map;

/**
 * 针对表【borrow_records(借阅记录表)】的数据库操作Mapper
 */
public interface BorrowRecordsMapper extends BaseMapper<BorrowRecords> {

    /**
     * 查询借阅列表
     */
    IPage<BorrowRecordVO> pageQuery(IPage<BorrowRecordVO> infoIPage, BorrowRecordQuery borrowRecordQuery);

    /**
     * 借阅记录查询（根据借阅记录 ID）
     */
    BorrowRecordVO getInfoById(@Param("id") Long id);

    /**
     * 查询个人阅读分类计数
     */
    @MapKey("category")
    List<Map<String, Object>> categoryCount(@Param("id") Long id);

    /**
     * 状态切换原子操作
     */
    int updateStatus(@Param("version") Integer version, @Param("newStatus") BorrowStatus newStatus, @Param("oldStatus") BorrowStatus... oldStatus);
}




