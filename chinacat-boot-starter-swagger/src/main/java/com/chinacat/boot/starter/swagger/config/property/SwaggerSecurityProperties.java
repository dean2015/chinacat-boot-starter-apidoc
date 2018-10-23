package com.chinacat.boot.starter.swagger.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinacat
 */
@Data
@ConfigurationProperties(prefix = SwaggerSecurityProperties.PREFIX)
public class SwaggerSecurityProperties {

    public static final String PREFIX = "smsf.swagger.security";

    private boolean enabled = false;

    private Map<String, ApiKey> schemes = new HashMap<>(0);

    private String scope = "global";

    private String scopeDescription = "Global authorization setting";

    @Data
    public static class ApiKey {
        String keyName;
        String passAs = "header";
    }

}