package cl.ipss.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Datos que aparecen en la cabecera de Swagger UI (http://localhost:8084/swagger-ui.html). */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("API Sistema de Biblioteca")
                .description("Búsqueda de libros, préstamos, devoluciones y disponibilidad - Java 21, Spring Boot, PostgreSQL")
                .version("1.0.0"));
    }
}
