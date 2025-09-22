package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Esta clase está destinada a poder generar contraseñas hasheadas para registrar "a mano" administradores en la base de datos.
public class PasswordHasher {

    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "Admin123!"; // Acá ponemos la contraseña en texto literal que queremos que tenga el administrador.
        String hashedPassword = passwordEncoder.encode(password); // Acá obtendremos el hash de la contraseña y la imprimimos por pantalla.
        System.out.println("Contraseña de administrador HASHEADA: " + hashedPassword);
    }
}

