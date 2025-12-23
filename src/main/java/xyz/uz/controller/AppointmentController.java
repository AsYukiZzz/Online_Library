package xyz.uz.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.uz.domain.query.AppointmentRecordQuery;
import xyz.uz.domain.vo.AppointmentRecordVO;
import xyz.uz.result.Result;
import xyz.uz.service.AppointmentRecordsService;

@Slf4j
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentRecordsService appointmentRecordsService;

    /**
     * 查询预约列表
     */
    @GetMapping
    public Result<IPage<AppointmentRecordVO>> pageQuery(AppointmentRecordQuery appointmentRecordQuery){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("书籍预约查询，查询者id={}",userId);
        IPage<AppointmentRecordVO> appointmentRecordVOIPage = appointmentRecordsService.pageQuery(appointmentRecordQuery);
        return Result.ok(appointmentRecordVOIPage);
    }

    /**
     * 预约
     */
    @PostMapping("/{bookId}")
    public Result<Object> reserve(@PathVariable Long bookId){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户id={}对书籍id={}预约",userId,bookId);
        appointmentRecordsService.reserve(userId,bookId);
        return Result.ok();
    }

    /**
     * 取消预约
     */
    @PutMapping("/{id}")
    public Result<Object> cancel(@PathVariable Long id){
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户id={}取消id={}的预约",userId,id);
        appointmentRecordsService.cancel(userId,id);
        return Result.ok();
    }
}
