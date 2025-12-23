package xyz.uz.config.mybatis;

import cn.dev33.satoken.exception.SaTokenContextException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * 公有字段填充配置
 */

@Slf4j
@Component
public class MetaObjectHandlerImpl implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充-插入");

        Long userId;
        try{
            // 尝试获取 UserId, 未能获取说明当前是注册请求，赋默认值 OL
            userId = StpUtil.getLoginId(0L);
        }catch (SaTokenContextException e){
            // 在非 Web 环境中无法获取Context，默认赋0L（系统执行）
            userId = 0L;
        }

        // 创建人
        this.strictInsertFill(metaObject, "createUser", Long.class, userId);

        // 创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());

        // 更新人
        this.strictInsertFill(metaObject, "updateUser", Long.class, userId);

        // 更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充-更新");

        Long userId;
        try{
            // 尝试获取 UserId, 未能获取说明当前是注册请求，赋默认值 OL
            userId = StpUtil.getLoginId(0L);
        }catch (SaTokenContextException e){
            // 在非 Web 环境中无法获取Context，默认赋0L（系统执行）
            userId = 0L;
        }

        // 更新人
        this.setFieldValByName("updateUser", userId, metaObject);

        // 更新时间
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
