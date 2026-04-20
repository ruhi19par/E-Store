package com.example.lcwd.Electronic.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // This is the identifier for the security scheme
        String schemeName = "bearerScheme";

        return new OpenAPI()
                // 1. Give your API a title and description
                .info(new Info()
                        .title("Electronic Store API")
                        .version("1.0")
                        .description("API documentation for the Electronic Store application"))

                // 2. Add the global security requirement (This puts the padlock on your endpoints)
                .addSecurityItem(new SecurityRequirement().addList(schemeName))

                // 3. Define the security scheme (Tell Swagger it's a JWT Bearer token)
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")));
    }
}