package com.chinacat.boot.starter.swagger.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chinacat
 */
@Data
@ConfigurationProperties(prefix = SwaggerInfoProperties.PREFIX)
public class SwaggerInfoProperties {

    public static final String PREFIX = "chinacat.swagger";

    private String groupName = "China Cat";

    private String basePackage = "";

    private String author = "China Cat";

    private String homepage = "https://github.com/dean2015";

    private String email = "";

    private String title = "China CAT API docs provider";

    private String description = "";

    private String version = "1.0.0";

    private String license = "None";

    private String licenseUrl = "";

    private String termsOfServiceUrl = "https://github.com/dean2015";

}