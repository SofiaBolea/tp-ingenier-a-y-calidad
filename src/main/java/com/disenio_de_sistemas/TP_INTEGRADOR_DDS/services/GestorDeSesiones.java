package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Usuario;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.UsuarioDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.UsuarioDAO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices.ValidadorAutenticacionUsuario;

@Service
public class GestorDeSesiones {

    private final UsuarioDAO usuarioDAO;
    private final ValidadorAutenticacionUsuario validadorAutenticacionUsuario;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias:
    @Autowired
    public GestorDeSesiones(UsuarioDAO usuarioDAO, ValidadorAutenticacionUsuario validadorAutenticacionUsuario, PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.validadorAutenticacionUsuario = validadorAutenticacionUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo para validar credenciales y autenticar usuarios:
    public Usuario autenticarUsuario(UsuarioDTO usuarioDTO) {

        // Encapsulamos la lógica de validación de datos de entrada dentro del mismo metodo de autenticación de usuario:
        try {
            validadorAutenticacionUsuario.validarUsuarioDTO(usuarioDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        // El metodo busca directamente en ambas tablas, bedel y administrador, para ver si encuentra alguna coincidencia:
        Usuario usuario = usuarioDAO.findByNombreUsuario(usuarioDTO.getNombreUsuario());

        // Verificamos si el usuario existe y si la contraseña es válida:
        if (usuario != null && passwordEncoder.matches(usuarioDTO.getContrasenia(), usuario.getContrasenia())) {
            return usuario;
        }

        throw new IllegalArgumentException("Nombre de usuario o contraseña incorrectos.");
    }

}