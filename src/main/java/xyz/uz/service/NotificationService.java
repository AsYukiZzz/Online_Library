package xyz.uz.service;

import xyz.uz.domain.entity.Notification;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.uz.domain.vo.WebsocketMessageVO;

import java.util.List;

/**
* 针对表【notification(通知表)】的数据库操作Service
*/
public interface NotificationService extends IService<Notification> {

    /**
     * 向指定用户发送通知
     */
    void sendToUser(Long userId, WebsocketMessageVO msg);

    /**
     * 向管理员广播
     */
    void broadcastToAdmin(WebsocketMessageVO msg);

    /**
     * 用户获取未读消息
     */
    List<WebsocketMessageVO> getUnread(Long id);
}
