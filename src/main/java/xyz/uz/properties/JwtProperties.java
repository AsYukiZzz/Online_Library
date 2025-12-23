package xyz.uz.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *  Jwt 属性封装（已过时）
 */

@Data
@Component
@Deprecated
//@ConfigurationProperties(prefix = "custom.jwt")
public class JwtProperties {

    private String key;
    private Integer expirationTime;
}
