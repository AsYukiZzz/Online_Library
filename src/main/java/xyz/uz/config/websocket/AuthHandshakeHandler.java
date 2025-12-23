package xyz.uz.config.websocket;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import xyz.uz.exception.BusinessException;
import xyz.uz.exception.code.SystemExceptionCode;

import java.security.Principal;
import java.util.Map;

@Slf4j
public class AuthHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 1. 尝试获取UserId
        String userId = StpUtil.getLoginIdAsString();

        // 2. 健壮性校验
        if (userId == null){
            throw new BusinessException(SystemExceptionCode.UNEXPECTED_STATE);
        }

        // 3. 存入令牌
        log.info("用户id={}已连接 WebSocket",userId);
        return new WebSocketPrincipal(userId);
    }
}
