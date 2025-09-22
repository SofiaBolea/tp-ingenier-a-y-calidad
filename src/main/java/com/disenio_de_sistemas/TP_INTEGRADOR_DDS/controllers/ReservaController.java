package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.controllers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.AulaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.ReservaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.DocenteDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.CatedraDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.GestorDeReservas;

@RestController
@RequestMapping("/reserva") // Ruta base para todas las operaciones de Reserva.
public class ReservaController {

    private final GestorDeReservas gestorDeReservas;

    // Inyección de dependencia:
    @Autowired
    public ReservaController(GestorDeReservas gestorDeReservas) {
        this.gestorDeReservas = gestorDeReservas;
    }

    @GetMapping("/docentes") // Ruta específica para obtener la lista de docentes.
    public ResponseEntity<Map<String, Object>> obtenerDocentes() {

        Map<String, Object> response = new HashMap<>();

        try {

            List<DocenteDTO> docentes = gestorDeReservas.obtenerDocentes();

            if (docentes.isEmpty()) {
                response.put("message", "No se encontraron docentes.");
                return ResponseEntity.ok(response);
            }

            response.put("message", "Docentes encontrados exitosamente.");
            response.put("data", docentes);
            return ResponseEntity.ok(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/catedras") // Ruta específica para obtener la lista de cátedras, seminarios y cursos.
    public ResponseEntity<Map<String, Object>> obtenerCatedras() {

        Map<String, Object> response = new HashMap<>();

        try {

            List<CatedraDTO> catedras = gestorDeReservas.obtenerCatedras();

            if (catedras.isEmpty()) {
                response.put("message", "No se encontraron cátedras, seminarios o cursos.");
                return ResponseEntity.ok(response);
            }

            response.put("message", "Cátedras, seminarios y cursos encontrados exitosamente.");
            response.put("data", catedras);
            return ResponseEntity.ok(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/validarDatosRenglones") // Ruta específica para validar los datos de los renglones de una reserva (primera página).
    public ResponseEntity<Map<String, String>> validarPrimeraPagina(@RequestBody ReservaDTO reservaDTO) {

        Map<String, String> response = new HashMap<>();
        try {

            gestorDeReservas.validarRenglones(reservaDTO);

            response.put("message", ""); // No retornamos ningún mensaje de exito, ya que sólo se necesita que, de ser válidos los datos, se pueda pasar a la siguiente página.
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) { // Capturamos errores de validación con el mensaje específico según tipo de error:
            response.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/obtenerDisponibilidad") // Ruta específica para validar los datos generales de una reserva (segunda página) y obtener la disponibilidad de aulas (o superposición de reservas).
    public ResponseEntity<Map<String, Object>> obtenerDisponibilidad(@RequestBody ReservaDTO reservaDTO) {

        Map<String, Object> response = new HashMap<>();

        try {

            // La lógica de validación de datos está embebida en el metodo del gestor.
            HashMap<String, Object> resultado = gestorDeReservas.validarDatosYObtenerDisponibilidadAulas(reservaDTO);

            List<List<AulaDTO>> aulasTotalesDTO = (List<List<AulaDTO>>) resultado.get("aulasTotalesDTO"); // Usamos una lista de listas<AulaDTO> ya que en pantalla necesitamos mostrar un listado con aulas por cada renglón de la lista de renglones para el que se encontró disponibilidad.
            List<List<ReservaDTO>> reservasSuperpuestas = (List<List<ReservaDTO>>) resultado.get("reservasSuperpuestas"); // Usamos una lista de listas<ReservaDTO> ya que en pantalla necesitamos mostrar un listado con reservas que se superponen por cada renglón de la lista de renglones.

            if(!reservasSuperpuestas.isEmpty()) {
                // Si no hay disponibilidad para al menos un renglón, retornamos las reservas superpuestas:
                response.put("message", "Reservas superpuestas encontradas.");
                response.put("data", reservasSuperpuestas);
            } else {
                // Si hay disponibilidad para todos los renglones, retornamos las aulas encontradas:
                response.put("message", "Aulas encontradas exitosamente.");
                response.put("data", aulasTotalesDTO);
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) { // Capturamos errores de validación con el mensaje específico según tipo de error:
            response.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/registrarReserva") // Ruta específica para persistir una nueva Reserva.
    public ResponseEntity<Map<String, Object>> registrarReserva(@RequestBody ReservaDTO reservaDTO) {

        Map<String, Object> response = new HashMap<>();

        try {

            gestorDeReservas.registrarReserva(reservaDTO);

            response.put("message", "Reserva registrada exitosamente.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) { // Capturamos errores de validación con el mensaje específico según tipo de error:
            response.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}