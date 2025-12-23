package xyz.uz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.uz.domain.entity.Notification;
import xyz.uz.domain.vo.WebsocketMessageVO;
import xyz.uz.enums.ReadingStatus;
import xyz.uz.mapper.NotificationMapper;
import xyz.uz.service.NotificationService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 针对表【notification(通知表)】的数据库操作Service实现
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 向指定用户发送通知
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void sendToUser(Long userId, WebsocketMessageVO msg) {
        // 1. 封装默认信息
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setTitle(msg.getTitle());
        notification.setContent(msg.getContent());
        notification.setType(msg.getType());
        notification.setIsRead(ReadingStatus.UNREAD);

        notificationMapper.insert(notification);

        // 2. 尝试发送通知
        String id = String.valueOf(userId);

        // 2.1 检查目标用户是否在线
        SimpUser user = simpUserRegistry.getUser(id);

        // 2.1.1 用户不在线：不再执行任何操作
        if (user == null) {
            return;
        }

        try {
            // 2.1.2 用户在线：直接发送消息，并将消息状态更改为已读
            simpMessagingTemplate.convertAndSendToUser(id, "/queue/reminder", msg);

            notification.setIsRead(ReadingStatus.READ);
            notificationMapper.updateById(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向管理员广播
     * 父事务失败会子事务失败，但子事务失败，不会导致父事务失败（父事务对异常进行显式捕获）
     */
    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void broadcastToAdmin(WebsocketMessageVO msg) {

        Notification notification = new Notification();

        // 1. 封装默认信息并插入到数据库
        notification.setUserId(0L);
        notification.setTitle(msg.getTitle());
        notification.setContent(msg.getContent());
        notification.setType(msg.getType());
        notification.setIsRead(ReadingStatus.READ);

        notificationMapper.insert(notification);

        // 2. 发送通知
        simpMessagingTemplate.convertAndSend("/topic/admin", msg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WebsocketMessageVO> getUnread(Long id) {
        // 1. 获取用户的未读通知列表
        List<Notification> notificationList = notificationMapper.selectList(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, id)
                        .eq(Notification::getIsRead, ReadingStatus.UNREAD)
                        .orderByDesc(Notification::getCreateTime)
        );

        // 2. 五维度消息直接返回
        if (notificationList == null || notificationList.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 格式转换，收集ID并进行批量更新
        List<Long> ids = notificationList.stream()
                .map(Notification::getId)
                .collect(Collectors.toList());

        List<WebsocketMessageVO> msgList = notificationList.stream().map(notification -> {
            WebsocketMessageVO msg = new WebsocketMessageVO();
            BeanUtils.copyProperties(notification, msg);
            return msg;
        }).collect(Collectors.toList());

        notificationMapper.update(null,
                new LambdaUpdateWrapper<Notification>()
                        .set(Notification::getIsRead, ReadingStatus.READ)
                        .in(Notification::getId, ids)
        );

        // 4. 返回消息
        return msgList;
    }
}




