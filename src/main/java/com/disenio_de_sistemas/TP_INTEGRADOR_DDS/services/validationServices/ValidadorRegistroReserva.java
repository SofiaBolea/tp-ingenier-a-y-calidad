package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices;

import java.time.LocalDate;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.ReservaDTO;

@Service
public class ValidadorRegistroReserva {

    // Expresiones regulares para las validaciones:
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"); // Validaciones varias para correos electrónicos.

    // Restricciones de longitud para los campos que hay que ingresar números:
    private static final int MAX_CAPACIDAD = 300; // Valor máximo tomado por convención.
    private static final int LONGITUD_MAX_MAIL = 80; // Longitud tomada por convención.

    public void validarRenglones(ReservaDTO reservaDTO) {

        // Validamos campos vacíos para renglones de la reserva:
        if ("PERIODICA".equals(reservaDTO.getTipoReserva())) {
            reservaDTO.getDiasSeleccionados().forEach(dia -> {
                if (dia.getDuracion() == 0) {
                    throw new IllegalArgumentException("El campo de duración es obligatorio.");
                }
            });
        } else if ("ESPORADICA".equals(reservaDTO.getTipoReserva())) {
            LocalDate fechaActual = LocalDate.now();
            reservaDTO.getFechasEsporadicas().forEach(fecha -> {
                if (fecha.getDuracion() == 0) {
                    throw new IllegalArgumentException("El campo de duración es obligatorio.");
                }
                if (fecha.getFecha().isBefore(fechaActual)) {
                    throw new IllegalArgumentException("Todas las fechas seleccionadas deben ser iguales o posteriores a la fecha actual.");
                }
            });
        }

        // Aclaración: toda la validación en torno a no seleccionar tipo de reserva, período, etc., se hace en el frontend (no se muestra el botón 'Siguiente' hasta seleccionar mínimamente algo).

    }

    public void validarDatosReserva(ReservaDTO reservaDTO) {

        // Validamos campos vacíos:
        if (reservaDTO.getTipoDeAula() == null || reservaDTO.getTipoDeAula().isBlank() ||
                reservaDTO.getDocente() == null || reservaDTO.getDocente().isBlank() ||
                reservaDTO.getNombreCatedra() == null || reservaDTO.getNombreCatedra().isBlank() ||
                reservaDTO.getCorreoElectronico() == null || reservaDTO.getCorreoElectronico().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        // Validamos que la cantidad de alumnos sea un entero positivo:
        if (reservaDTO.getCantidadAlumnos() <= 0) {
            throw new IllegalArgumentException("La cantidad de alumnos debe ser un número entero positivo.");
        }

        // Validamos que el campo 'Cantidad de alumnos' no sea un número mayor a MAX_CAPACIDAD:
        if (reservaDTO.getCantidadAlumnos() > MAX_CAPACIDAD) {
            throw new IllegalArgumentException("La cantidad de alumnos debe ser un número menor o igual a '" + MAX_CAPACIDAD + "'.");
        }

        // Validamos que el correo electrónico cumpla con la expresión regular especificada (un correo electrónico válido):
        if (!PATRON_CORREO.matcher(reservaDTO.getCorreoElectronico()).matches()) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }

        // Validamos que el correo electrónico tenga una longitud máxima de LONGITUD_MAX_MAIL:
        if (reservaDTO.getCorreoElectronico().length() > LONGITUD_MAX_MAIL) {
            throw new IllegalArgumentException("El correo electrónico debe tener " + LONGITUD_MAX_MAIL + " caracteres como máximo.");
        }

    }

}