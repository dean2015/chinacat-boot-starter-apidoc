package com.chinacat.boot.starter.swagger.config;

import com.chinacat.boot.starter.swagger.config.property.SwaggerInfoProperties;
import com.chinacat.boot.starter.swagger.config.property.SwaggerSecurityProperties;
import com.chinacat.boot.starter.web.core.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chinacat
 */
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {

    @Autowired
    private SwaggerInfoProperties swaggerInfoProperty;

    @Autowired
    private SwaggerSecurityProperties swaggerSecurityProperties;

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(swaggerInfoProperty.getGroupName())
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .apiInfo(apiInfo());
        if (swaggerSecurityProperties.isEnabled()) {
            docket = docket
                    .securitySchemes(securitySchemes())
                    .securityContexts(securityContexts());
        }
        String basePackage = swaggerInfoProperty.getBasePackage();
        ApiSelectorBuilder apiBuilder = null;
        if (StringUtils.isBlank(basePackage)) {
            apiBuilder = docket.select()
                    .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class));
        } else {
            apiBuilder = docket.select().apis(RequestHandlerSelectors.basePackage(basePackage));
        }
        return apiBuilder.build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> securitySchemes = new ArrayList<>(swaggerSecurityProperties.getSchemes().size());
        swaggerSecurityProperties.getSchemes().forEach((name, apikey) -> {
            securitySchemes.add(new ApiKey(name,
                    apikey.getKeyName(),
                    apikey.getPassAs()));

        });
        return securitySchemes;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>(1);
        securityContexts.add(SecurityContext.builder()
                .securityReferences(securityReferences())
                .build());
        return securityContexts;
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope[] authorizationScopes = {
                new AuthorizationScope(swaggerSecurityProperties.getScope(),
                        swaggerSecurityProperties.getScopeDescription())
        };
        List<SecurityReference> securityReferences = new ArrayList<>(swaggerSecurityProperties.getSchemes().size());
        swaggerSecurityProperties.getSchemes().forEach((name, apikey) -> {
            securityReferences.add(new SecurityReference(name, authorizationScopes));
        });
        return securityReferences;
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(swaggerInfoProperty.getAuthor(), swaggerInfoProperty.getHomepage(),
                swaggerInfoProperty.getEmail());
        return new ApiInfoBuilder().title(swaggerInfoProperty.getTitle())
                .description(swaggerInfoProperty.getDescription()).license(swaggerInfoProperty.getLicense())
                .licenseUrl(swaggerInfoProperty.getLicenseUrl()).contact(contact)
                .termsOfServiceUrl(swaggerInfoProperty.getTermsOfServiceUrl())
                .version(swaggerInfoProperty.getVersion()).build();
    }

}
