package xyz.uz.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.uz.domain.entity.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.uz.domain.vo.WebsocketMessageVO;

import java.util.List;

/**
* 针对表【notification(通知表)】的数据库操作Mapper
*/
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 查询用户未读消息
     */
    @Select("select id, user_id, title, content, type, is_read, create_user, create_time, update_user, update_time, is_delete, version from notification where is_read = 0 and user_id = #{userId}")
    List<WebsocketMessageVO> getUnread(@Param("userId") Long id);
}




