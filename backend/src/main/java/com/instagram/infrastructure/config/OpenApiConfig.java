package com.instagram.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI configuration.
 * Access at: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI socialMediaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Social Media Platform API")
                        .description("Hexagonal Architecture — Spring Boot 3 Backend")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Dev Team")
                                .email("dev@social-media.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
