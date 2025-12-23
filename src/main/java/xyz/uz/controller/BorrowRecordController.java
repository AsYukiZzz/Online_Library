package xyz.uz.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.uz.anno.CheckRole;
import xyz.uz.domain.dto.BorrowApplicationDTO;
import xyz.uz.domain.dto.ReviewResultDTO;
import xyz.uz.domain.entity.BorrowRecords;
import xyz.uz.domain.query.BorrowRecordQuery;
import xyz.uz.domain.vo.BorrowRecordVO;
import xyz.uz.enums.UserPermission;
import xyz.uz.result.Result;
import xyz.uz.service.BorrowRecordsService;

/**
 * 借阅记录 Controller
 */

@Slf4j
@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordsService borrowRecordsService;

    /**
     * 用户查询借阅列表
     */
    @PostMapping("/list")
    public Result<IPage<BorrowRecordVO>> pageQuery(BorrowRecordQuery borrowRecordQuery){

        IPage<BorrowRecordVO> borrowRecordIPage;
        long userId = StpUtil.getLoginIdAsLong();

        // 1. 判断用户是否具有管理员权限
        if (StpUtil.hasRole(UserPermission.ADMIN.getRoleName())){
            log.info("管理员id={}查询借阅列表",userId);
            borrowRecordIPage = borrowRecordsService.pageQueryAdmin(borrowRecordQuery);
        }

        // 2. 若为用户权限，则执行用户查询
        else {
            log.info("用户id={}查询借阅列表",userId);
            borrowRecordIPage = borrowRecordsService.pageQueryUser(borrowRecordQuery);
        }

        return Result.ok(borrowRecordIPage);
    }

    /**
     * 借阅记录查询（根据借阅记录 ID）
     */
    @GetMapping("/{id}")
    public Result<BorrowRecordVO> getInfoById(@PathVariable Long id){
        log.info("用户id={}查询id={}的借阅记录信息",StpUtil.getLoginIdAsLong(),id);
        return Result.ok(borrowRecordsService.getInfoById(id));
    }

    /**
     * 查询待处理的借阅申请数量（管理员）
     */
    @GetMapping("/count")
    @CheckRole(UserPermission.ADMIN)
    public Result<Integer> getBorrowCount() {
        long count = borrowRecordsService.count(new LambdaQueryWrapper<BorrowRecords>().eq(BorrowRecords::getStatus, 0));
        return Result.ok((int) count);
    }

    /**
     * 发出借阅申请
     */
    @PostMapping("")
    public Result<Object> borrow(@RequestBody BorrowApplicationDTO borrowApplicationDTO){
        log.info("用户id={}发出借阅申请", StpUtil.getLoginIdAsLong());
        borrowRecordsService.borrow(borrowApplicationDTO);
        return Result.ok();
    }

    /**
     * 借阅申请处理
     */
    @PutMapping("/process")
    @CheckRole(UserPermission.ADMIN)
    public Result<Object> applicantProcess(@RequestBody ReviewResultDTO reviewResultDTO){
        log.info("管理员id={}对借阅申请id={}进行审核",StpUtil.getLoginIdAsLong(),reviewResultDTO.getId());
        borrowRecordsService.applicantProcess(reviewResultDTO);
        return Result.ok();
    }

    /**
     * 取书确认
     */
    @PutMapping("/pickup/{id}")
    @CheckRole(UserPermission.USER)
    public Result<Object> pickupConfirm(@PathVariable Long id){
        log.info("用户id={}确认借阅记录id={}的书被取走",StpUtil.getLoginIdAsLong(),id);
        borrowRecordsService.pickupConfirm(id);
        return Result.ok();
    }

    /**
     * 还书确认
     */
    @PutMapping("/return/{id}")
    @CheckRole(UserPermission.ADMIN)
    public Result<Object> returnConfirm(@PathVariable Long id){
        log.info("管理员id={}确认借阅记录id={}的书被归还",StpUtil.getLoginIdAsLong(),id);
        borrowRecordsService.returnConfirm(id);
        return Result.ok();
    }
}
