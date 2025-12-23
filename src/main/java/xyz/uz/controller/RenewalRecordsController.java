package xyz.uz.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.uz.anno.CheckRole;
import xyz.uz.domain.dto.RenewalApplicationDTO;
import xyz.uz.domain.dto.ReviewResultDTO;
import xyz.uz.domain.entity.RenewalRecords;
import xyz.uz.domain.query.RenewalRecordQuery;
import xyz.uz.domain.vo.RenewalRecordVO;
import xyz.uz.enums.UserPermission;
import xyz.uz.result.Result;
import xyz.uz.service.RenewalRecordsService;

@Slf4j
@RestController
@RequestMapping("/renew")
public class RenewalRecordsController {

    @Autowired
    private RenewalRecordsService renewalRecordsService;

    /**
     * 查看续借请求详细信息（根据ID）
     */
    @GetMapping("/{id}")
    public Result<RenewalRecordVO> getInfoById(@PathVariable Long id){
        log.info("用户id={}查询id={}的续借请求详细信息",StpUtil.getLoginIdAsLong(),id);
        return Result.ok(renewalRecordsService.getInfoById(id));
    }

    /**
     * 查询续借申请列表（管理员）
     */
    @GetMapping("/list")
    @CheckRole(UserPermission.ADMIN)
    public Result<IPage<RenewalRecordVO>> pageQuery(RenewalRecordQuery renewalRecordQuery){
        log.info("管理员id={}获取续借请求列表",StpUtil.getLoginIdAsLong());
        return Result.ok(renewalRecordsService.pageQueryAdmin(renewalRecordQuery));
    }

    /**
     * 查询待处理续借申请数量（管理员）
     */
    @GetMapping("/count")
    @CheckRole(UserPermission.ADMIN)
    public Result<Integer> getRenewCount() {
        long count = renewalRecordsService.count(new LambdaQueryWrapper<RenewalRecords>().eq(RenewalRecords::getStatus, 0));
        return Result.ok((int) count);
    }

    /**
     * 发出续借申请
     */
    @PutMapping("/{id}")
    public Result<Object> renew(@RequestBody RenewalApplicationDTO renewalApplicationDTO){
        log.info("用户id={}发出续借申请", StpUtil.getLoginIdAsLong());
        renewalRecordsService.renew(renewalApplicationDTO);
        return Result.ok();
    }

    /**
     * 续借申请处理
     */
    @PutMapping("/process")
    public Result<Object> applicantProcess(@RequestBody ReviewResultDTO reviewResultDTO){
        log.info("管理员id={}对续借申请id={}进行审核",StpUtil.getLoginIdAsLong(),reviewResultDTO.getId());
        renewalRecordsService.applicantProcess(reviewResultDTO);
        return Result.ok();
    }
}
