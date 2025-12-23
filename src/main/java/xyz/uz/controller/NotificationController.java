package xyz.uz.controller;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.uz.anno.CheckRole;
import xyz.uz.domain.vo.WebsocketMessageVO;
import xyz.uz.enums.UserPermission;
import xyz.uz.result.Result;
import xyz.uz.service.NotificationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 用户获取未读消息
     */
    @GetMapping
    @CheckRole(UserPermission.USER)
    public Result<List<WebsocketMessageVO>> getUnread(){
        Long id = StpUtil.getLoginIdAsLong();
        log.info("用户id={}获取未读消息", id);
        return Result.ok(notificationService.getUnread(id));
    }
}
