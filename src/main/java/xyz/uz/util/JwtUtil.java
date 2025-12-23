package xyz.uz.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.uz.properties.JwtProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具类，Jwt令牌生成与解析
 * 已过时，现使用 sa-token 的 jwt 插件
 */

@Deprecated
@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    public String generateJwtToken(Object loginId, String loginType) {
        return this.generateJwtToken(
                new HashMap<String, Object>() {{
                    put("loginId", loginId);
                    put("loginType", loginType);
                }}
        );
    }

    /**
     * 构造 JWT 令牌
     *
     * @return JWT 令牌字符串
     */
    public String generateJwtToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getKey())
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .compact();
    }

    /**
     * 解析 JWT 令牌
     *
     * @return 构造 JWT 令牌时附带的负载
     */
    public Map<String, Object> parseJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
