package xyz.uz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.uz.domain.entity.BorrowRecords;
import xyz.uz.domain.vo.PersonalDataOverviewVO;
import xyz.uz.enums.BorrowStatus;
import xyz.uz.mapper.BorrowRecordsMapper;
import xyz.uz.service.BorrowRecordsService;
import xyz.uz.service.ReportService;

import java.util.List;
import java.util.Map;

/**
 * 报表 ServiceImpl
 */

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private BorrowRecordsService borrowRecordsService;

    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;

    /**
     * 查看个人数据总览
     */
    @Override
    public PersonalDataOverviewVO getOverview(long id) {

        // 1. 查询累计借阅次数
        long borrowCount = borrowRecordsService.count(
                new QueryWrapper<BorrowRecords>()
                        .eq("status", BorrowStatus.RETURNED)
                        .eq("student_id", id)
        );

        // 2. 查询按时归还次数
        long returnOnTimeCount = borrowRecordsService.count(
                new QueryWrapper<BorrowRecords>()
                        .eq("status", BorrowStatus.RETURNED)
                        .eq("student_id", id)
        );

        // 3. 查询逾期次数
        long overdueCount = borrowRecordsService.count(
                new QueryWrapper<BorrowRecords>()
                        .eq("status", BorrowStatus.OVERDUE)
                        .eq("student_id", id)
        );

        // 4. 统计阅读偏好数据
        List<Map<String, Object>> categoryCountList = borrowRecordsMapper.categoryCount(id);

        // 5. 查询最近在读五本
        List<BorrowRecords> list = borrowRecordsService.list(
                new QueryWrapper<BorrowRecords>()
                        .eq("student_id", id)
                        .in("status", BorrowStatus.LOANED, BorrowStatus.OVERDUE)
                        .orderByDesc("review_time")
                        .last("limit 5")
        );

        // 6，封装数据
        return PersonalDataOverviewVO.builder()
                .borrowCount(String.valueOf(borrowCount))
                .returnOnTimeCount(String.valueOf(returnOnTimeCount))
                .overdueCount(String.valueOf(overdueCount))
                .categoryCount(categoryCountList)
                .build();
    }
}
