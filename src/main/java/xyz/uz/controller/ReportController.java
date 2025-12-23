package xyz.uz.controller;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.uz.domain.vo.PersonalDataOverviewVO;
import xyz.uz.result.Result;
import xyz.uz.service.BorrowRecordsService;
import xyz.uz.service.ReportService;

/**
 * 报表相关解耦
 */

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private BorrowRecordsService borrowRecordsService;

    @Autowired
    private ReportService reportService;

    /**
     * 查看个人数据总览
     */
    @GetMapping("/user/overview")
    public Result<PersonalDataOverviewVO> getOverview() {
        long id = StpUtil.getLoginIdAsLong();
        log.info("用户id={}查询个人数据总览", id);
        PersonalDataOverviewVO personalDataOverviewVO = reportService.getOverview(id);
        return Result.ok(personalDataOverviewVO);
    }
}
