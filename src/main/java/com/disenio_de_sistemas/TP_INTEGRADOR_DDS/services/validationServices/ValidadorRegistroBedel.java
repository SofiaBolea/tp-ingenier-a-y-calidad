package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices;

import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.BedelDTO;

@Service
public class ValidadorRegistroBedel {

    // Expresiones regulares para las validaciones:
    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[A-Za-záéíóúÁÉÍÓÚüÜ]+( [A-Za-záéíóúÁÉÍÓÚüÜ]+)*$"); // Solo letras y vocales con tilde, además de espacios en blanco en cualquier parte menos al inicio y final (para segundos nombres).
    private static final Pattern PATRON_USERNAME = Pattern.compile("^[A-Za-z]+[A-Za-z0-9._]*$"); // Letras, números, puntos y guiones bajos (empezando por letra).
    private static final Pattern PATRON_CONTRASENIA = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"); // Más de 8 caracteres, al menos una mayuscula, un numero y un simbolo especial.

    // Restricciones de longitud para los campos que hay que ingresar cadenas de caracteres:
    private static final int LONGITUD_MAX_NOMBRE = 35; // Longitud tomada por convención (considerando nombres compuestos o segundos nombres).
    private static final int LONGITUD_MAX_USERNAME = 30; // Longitud tomada por convención.
    private static final int LONGITUD_MAX_CONTRASENIA = 32; // Longitud tomada por convención.

    public void validarBedelDTO(BedelDTO bedelDTO) throws IllegalArgumentException {

        // Validamos campos vacíos:
        if (bedelDTO.getNombre() == null || bedelDTO.getNombre().trim().isBlank() ||
                bedelDTO.getApellido() == null || bedelDTO.getApellido().trim().isBlank() ||
                bedelDTO.getNombreUsuario() == null || bedelDTO.getNombreUsuario().trim().isBlank() ||
                bedelDTO.getContrasenia() == null || bedelDTO.getContrasenia().trim().isBlank() ||
                bedelDTO.getTurnoDeTrabajo() == null || bedelDTO.getConfirmacionContrasenia() == null ||
                bedelDTO.getConfirmacionContrasenia().trim().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        // Validamos que el campo 'Nombre' solo contenga letras:
        if (!PATRON_NOMBRE.matcher(bedelDTO.getNombre()).matches()) {
            throw new IllegalArgumentException("El nombre debe contener solo letras (se permiten tíldes).");
        }

        // Validamos que el campo 'Nombre' tenga una longitud máxima de LONGITUD_MAX_NOMBRE:
        if (bedelDTO.getNombre().length() > LONGITUD_MAX_NOMBRE) {
            throw new IllegalArgumentException("El nombre debe tener " + LONGITUD_MAX_NOMBRE + " caracteres como máximo.");
        }

        // Validamos que el campo 'Apellido' solo contenga letras:
        if (!PATRON_NOMBRE.matcher(bedelDTO.getApellido()).matches()) {
            throw new IllegalArgumentException("El apellido debe contener solo letras (se permiten tíldes).");
        }

        // Validamos que el campo 'Apellido' tenga una longitud máxima de LONGITUD_MAX_NOMBRE:
        if (bedelDTO.getApellido().length() > LONGITUD_MAX_NOMBRE) {
            throw new IllegalArgumentException("El apellido debe tener " + LONGITUD_MAX_NOMBRE + " caracteres como máximo.");
        }

        // Validamos que nombre el nombre de usuario empiece con una letra, y después sean cuantas letras o numeros se quieran:
        if (!PATRON_USERNAME.matcher(bedelDTO.getNombreUsuario()).matches()) {
            throw new IllegalArgumentException("El nombre de usuario debe contener al menos una letra al comienzo (sin tíldes) y luego letras, números, puntos o guiones bajos.");
        }

        // Validamos que el campo 'Nombre de Usuario' o (identificador) tenga una longitud máxima de LONGITUD_MAX_NOMBRE:
        if (bedelDTO.getNombreUsuario().length() > LONGITUD_MAX_USERNAME) {
            throw new IllegalArgumentException("El nombre de usuario debe tener " + LONGITUD_MAX_USERNAME + " caracteres como máximo.");
        }

        // Validamos contraseña:
        if (!PATRON_CONTRASENIA.matcher(bedelDTO.getContrasenia()).matches()) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un caracter especial.");
        }

        // Validamos que el campo 'Contraseña' tenga una longitud máxima de LONGITUD_MAX_CONTRASENIA:
        if (bedelDTO.getContrasenia().length() > LONGITUD_MAX_CONTRASENIA) {
            throw new IllegalArgumentException("La contraseña debe tener " + LONGITUD_MAX_CONTRASENIA + " caracteres como máximo.");
        }

        // Validamos que las contraseñas coincidan:
        if (!bedelDTO.getContrasenia().equals(bedelDTO.getConfirmacionContrasenia())) {
            throw new IllegalArgumentException("La contraseña y la confirmación de contraseña no coinciden.");
        }
    }

}