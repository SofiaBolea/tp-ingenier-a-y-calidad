package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices;

import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.BedelDTO;

@Service
public class ValidadorModificacionBedel {

    // Expresiones regulares para las validaciones:
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[A-Za-záéíóúÁÉÍÓÚüÜ]+( [A-Za-záéíóúÁÉÍÓÚüÜ]+)*$"); // Solo letras y vocales con tilde, además de espacios en blanco en cualquier parte menos al inicio y final (para segundos nombres).
    private static final Pattern PATRON_CONTRASENIA = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"); // Más de 8 caracteres, al menos una mayuscula, un numero y un simbolo especial.

    // Restricciones de longitud para los campos que hay que ingresar cadenas de caracteres:
    private static final int LONGITUD_MAX_NOMBRE = 35; // Longitud tomada por convención (considerando nombres compuestos o segundos nombres).
    private static final int LONGITUD_MAX_CONTRASENIA = 32; // Longitud tomada por convención.

    public void validarBedelDTO(BedelDTO bedelDTO) throws IllegalArgumentException {

        // Validamos campos vacíos:
        if (bedelDTO.getNombre() == null || bedelDTO.getNombre().trim().isBlank() ||
                bedelDTO.getApellido() == null || bedelDTO.getApellido().trim().isBlank() || bedelDTO.getTurnoDeTrabajo() == null) {
            throw new IllegalArgumentException("Los campos de nombre, apellido y turno no pueden estar vacíos.");
        }

        // Validamos que el campo 'Nombre' solo contenga letras:
        if (!PATRON_NOMBRE.matcher(bedelDTO.getNombre()).matches()) {
            throw new IllegalArgumentException("El nombre debe contener solo letras (se permiten tíldes).");
        }

        // Validamos que el campo 'Nombre' tenga una longitud máxima de MAX_NAME_LENGTH:
        if (bedelDTO.getNombre().length() > LONGITUD_MAX_NOMBRE) {
            throw new IllegalArgumentException("El nombre debe tener " + LONGITUD_MAX_NOMBRE + " caracteres como máximo.");
        }

        // Validamos que el campo 'Apellido' solo contenga letras:
        if (!PATRON_NOMBRE.matcher(bedelDTO.getApellido()).matches()) {
            throw new IllegalArgumentException("El apellido debe contener solo letras (se permiten tíldes).");
        }

        // Validamos que el campo 'Apellido' tenga una longitud máxima de MAX_NAME_LENGTH:
        if (bedelDTO.getApellido().length() > LONGITUD_MAX_NOMBRE) {
            throw new IllegalArgumentException("El apellido debe tener " + LONGITUD_MAX_NOMBRE + " caracteres como máximo.");
        }

        // Validamos contraseña y confirmación de contraseña:
        if (bedelDTO.getContrasenia() != null || bedelDTO.getConfirmacionContrasenia() != null) {
            if (bedelDTO.getContrasenia() == null || bedelDTO.getConfirmacionContrasenia() == null) {
                throw new IllegalArgumentException("Debe completar tanto la contraseña como la confirmación de contraseña.");
            }
            if (!bedelDTO.getContrasenia().equals(bedelDTO.getConfirmacionContrasenia())) {
                throw new IllegalArgumentException("La contraseña y la confirmación de contraseña no coinciden.");
            }
            if (!PATRON_CONTRASENIA.matcher(bedelDTO.getContrasenia()).matches()) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un caracter especial.");
            }
            // Validamos que el campo 'Contraseña' tenga una longitud máxima de MAX_PASSWORD_LENGTH:
            if (bedelDTO.getContrasenia().length() > LONGITUD_MAX_CONTRASENIA) {
                throw new IllegalArgumentException("La contraseña debe tener " + LONGITUD_MAX_CONTRASENIA + " caracteres como máximo.");
            }
        }

    }

}