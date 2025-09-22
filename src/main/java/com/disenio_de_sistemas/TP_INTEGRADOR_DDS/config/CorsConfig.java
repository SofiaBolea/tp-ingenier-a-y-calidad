package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permitimos CORS en todas las rutas.
                        .allowedOrigins("http://localhost:5173") // Acá sólo va el origen, sin el path.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Métodos permitidos.
                        .allowedHeaders("*") // Permitimos todos los headers.
                        .allowCredentials(true); // Permitimos credenciales (opcional).
            }
        };
    }
}