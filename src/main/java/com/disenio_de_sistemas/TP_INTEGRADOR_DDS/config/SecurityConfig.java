package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    // Bean de configuración para seguridad de bloqueo y acceso a endpoints:
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desactivamos CSRF si es necesario.
                .authorizeRequests()
                .anyRequest().permitAll(); // Permitimos todas las solicitudes sin autenticación (no discrinamos ningún endpoint en torno al bloqueo).

        return http.build();
    }

    // Bean de configuración para uso de funciones de hash, codificación y decodificación de contraseñas:
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


