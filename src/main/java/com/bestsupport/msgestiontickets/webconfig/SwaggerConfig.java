package com.bestsupport.msgestiontickets.webconfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi (){
        return new OpenAPI()
                .info(new Info()
                        .title("API 2025 Gestión Tickets Best Support")
                        .version("1.0")
                        .description("Documentación de la API para el sistema de gestión de tickets BEST SUPPORT.")
                        .contact(
                                new Contact()
                                        .name("Gabriel Serrano Mena")
                                        .email("ga.serranom@profesor.duoc.cl")
                                        .url("https://github.com/prof-gabote")
                        )
                        .summary("API de ejercicio para la asignatura FullStack I de DuocUC Viña del Mar")
                );
    }
}
