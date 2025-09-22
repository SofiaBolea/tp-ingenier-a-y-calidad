package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices;

import org.springframework.stereotype.Service;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.UsuarioDTO;

@Service
public class ValidadorAutenticacionUsuario {

    public void validarUsuarioDTO(UsuarioDTO usuarioDTO) throws IllegalArgumentException {

        // Validamos campos vacíos:
        if (usuarioDTO.getNombreUsuario() == null || usuarioDTO.getNombreUsuario().trim().isBlank() ||
                usuarioDTO.getContrasenia() == null || usuarioDTO.getContrasenia().trim().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

    }
}