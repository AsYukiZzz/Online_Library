package xyz.uz.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.uz.domain.entity.BorrowRecords;
import xyz.uz.domain.entity.User;
import xyz.uz.enums.BorrowStatus;
import xyz.uz.mapper.BorrowRecordsMapper;
import xyz.uz.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务
 */

@Slf4j
@Component
public class ScheduledTasks {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;

    /**
     * 逾期提醒：向约定还书日期前一天的客户发送通知
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void overdueReminder(){

        LocalDateTime now = LocalDateTime.now().plusDays(1L);
        List<BorrowRecords> records = borrowRecordsMapper.selectList(
                new QueryWrapper<BorrowRecords>()
                        .le("due_time", now)                        // 距离归还时间不足24h
                        .eq("status", BorrowStatus.LOANED)          // 书在学生手上
        );

        if (records == null || records.isEmpty()){
            return;
        }

        // todo 还书提醒
    }

    /**
     * 已经逾期：将用户状态设置为逾期
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void overdueSet(){

    }

    /**
     * 逾期恢复：将已将全部逾期记录消除的用户的状态置为正常
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void overdueCancel(){
        // 1. 获取存在逾期记录用户列表
        List<User> users = userMapper.selectList(
                new QueryWrapper<User>()
                        .eq("status",1)
        );

        if (users == null || users.isEmpty()){
            return;
        }

        for (User user : users) {
            BorrowRecords record = borrowRecordsMapper.selectOne(
                    new QueryWrapper<BorrowRecords>()
                            .eq("status", BorrowStatus.OVERDUE)
                            .eq("student_id", user.getId())
            );

            if (record == null){
                user.setIsOverdue(0);
                // 对逾期状态的设置仅有该定时任务，不进行乐观锁校验
                userMapper.updateById(user);
            }
        }
    }
}
