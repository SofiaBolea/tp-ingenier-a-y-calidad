package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.controllers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.AulaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.GestorDeAulas;

@RestController
@RequestMapping("/aula") // Ruta base para todas las operaciones de Aula.
public class AulaController {

    private final GestorDeAulas gestorDeAulas;

    // Inyección de dependencia:
    @Autowired
    public AulaController(GestorDeAulas gestorDeAulas) {
        this.gestorDeAulas = gestorDeAulas;
    }

    @PostMapping("/buscar") // Ruta específica para buscar un Aula.
    public ResponseEntity<Map<String, Object>> buscarAula(@RequestBody AulaDTO aulaDTO) {

        Map<String, Object> response = new HashMap<>();

        try {

            List<AulaDTO> aulas = gestorDeAulas.buscarAula(aulaDTO);

            if (aulas.isEmpty()) {
                response.put("message", "No se encontraron aulas que coincidan con los filtros especificados.");
                return ResponseEntity.ok(response);
            }

            response.put("message", "Aulas encontradas exitosamente.");
            response.put("data", aulas);
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

    @GetMapping("/obtenerAulas") // Ruta específica para obtener la lista entera de las aulas en la base de datos (para mostrar en la lista desplegable).
    public ResponseEntity<Map<String, Object>> obtenerIDAulas() {

        Map<String, Object> response = new HashMap<>();

        try {

            List<AulaDTO> aulas = gestorDeAulas.obtenerAulas();

            if (aulas.isEmpty()) {
                response.put("message", "No se encontraron aulas.");
                return ResponseEntity.ok(response);
            }

            response.put("message", "Aulas encontradas exitosamente.");
            response.put("data", aulas);
            return ResponseEntity.ok(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/obtenerTipos") // Ruta específica para obtener la lista de tipos de aulas que hay en la base de datos (para mostrar en la lista desplegable).
    public ResponseEntity<Map<String, Object>> obtenerTiposDeAula() {

        Map<String, Object> response = new HashMap<>();

        try {

            List<String> tiposDeAulas = gestorDeAulas.obtenerTipos();

            if (tiposDeAulas.isEmpty()) {
                response.put("message", "No se encontraron tipos de aulas.");
                return ResponseEntity.ok(response);
            }

            response.put("message", "Tipos de aulas encontrados exitosamente.");
            response.put("data", tiposDeAulas);
            return ResponseEntity.ok(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Usamos PATCH porque éste hace una actualización parcial (estamos modificando solo el atributo estado), y es lo más adecuado si solo queremos actualizar ese campo sin modificar el resto del objeto.
    @PatchMapping("/eliminar") // Ruta específica para eliminar LÓGICAMENTE un Aula (estado TRUE -> estado FALSE / estado HABILITADA -> estado DESHABILITADA).
    public ResponseEntity<Map<String, Object>> eliminarAula(@RequestBody AulaDTO aulaDTO) {

        Map<String, Object> response = new HashMap<>();

        try {

            gestorDeAulas.eliminarAula(aulaDTO);

            response.put("message", "Aula eliminada exitosamente.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException | NoSuchElementException ex) { // Capturamos errores de validación con el mensaje específico según tipo de error:
            response.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}