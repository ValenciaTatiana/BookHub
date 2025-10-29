package com.bookhub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookhubOpenAPI() {
        return new OpenAPI().info(
            new Info()
                .title("BookHub API")
                .description("Documentacion de la API para la gestion de libros, usuarios y prestamos.")
                .version("v1")
        );
    }
}
