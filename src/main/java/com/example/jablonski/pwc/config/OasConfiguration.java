package com.example.jablonski.pwc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("info.build")
@Setter
public class OasConfiguration {

    private String projectName;
    private String version;
    private String description;

    public static final String CODE_OWNER = "Michal Jablonski";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(getInfo());
    }

    private Info getInfo() {
        return new Info().title(projectName)
                .description(description)
                .version(version)
                .contact(new Contact().name(CODE_OWNER))
                .license(new License().name("Apache 2.0").url("http:localhost:8080"));
    }
}
