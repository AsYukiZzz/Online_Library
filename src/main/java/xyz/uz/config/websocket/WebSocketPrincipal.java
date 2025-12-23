package xyz.uz.config.websocket;

import java.security.Principal;

public class WebSocketPrincipal implements Principal {

    private final String userId;

    public WebSocketPrincipal(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return userId;
    }
}
