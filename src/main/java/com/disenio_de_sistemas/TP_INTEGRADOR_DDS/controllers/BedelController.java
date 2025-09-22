package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.controllers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.BedelDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.GestorDeBedeles;

@RestController
@RequestMapping("/bedel") // Ruta base para todas las operaciones de Bedel.
public class BedelController {

    private final GestorDeBedeles gestorDeBedeles;

    // Inyección de dependencia:
    @Autowired
    public BedelController(GestorDeBedeles gestorDeBedeles) {
        this.gestorDeBedeles = gestorDeBedeles;
    }

    @PostMapping("/registrar") // Ruta específica para registrar un Bedel.
    public ResponseEntity<Map<String, String>> registrarBedel(@RequestBody BedelDTO bedelDTO) {

        Map<String, String> response = new HashMap<>();

        try {

            gestorDeBedeles.registrarBedel(bedelDTO);

            response.put("message", "Bedel registrado exitosamente.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) { // Capturamos errores de validación con el mensaje específico según tipo de error:
            response.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) { // Manejo de otros errores genéricos:
            ex.printStackTrace();
            response.put("message", "Error interno en el servidor."); // Por ejemplo, si no se conectó bien el front con el back.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/buscar") // Ruta específica para buscar un Bedel.
    public ResponseEntity<Map<String, Object>> buscarBedel(@RequestBody BedelDTO bedelDTO) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Obtenemos los bedeles que coinciden con los filtros.
            List<BedelDTO> bedeles = gestorDeBedeles.buscarBedel(bedelDTO);

            // Obtenemos el total de bedeles en la base de datos.
            long totalBedeles = gestorDeBedeles.contarTotalBedeles();

            if (bedeles.isEmpty()) {
                response.put("message", "No se encontraron bedeles que coincidan con los filtros especificados.");
                response.put("totalResultados", totalBedeles); // Incluimos el total de bedeles aunque no haya resultados.
                return ResponseEntity.ok(response);
            }

            response.put("message", "Bedeles encontrados exitosamente.");
            response.put("data", bedeles);
            response.put("totalResultados", totalBedeles); // Incluimos el total de bedeles.

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

    // Usamos PATCH porque éste hace una actualización parcial (estamos modificando solo el atributo estado), y es lo más adecuado si solo queremos actualizar ese campo sin modificar el resto del objeto.
    @PatchMapping("/eliminar") // Ruta específica para eliminar LÓGICAMENTE un Bedel (estado TRUE -> estado FALSE).
    public ResponseEntity<Map<String, Object>> eliminarBedel(@RequestBody BedelDTO bedelDTO) {

        Map<String, Object> response = new HashMap<>();

        try {

            gestorDeBedeles.eliminarBedel(bedelDTO);

            response.put("message", "Bedel eliminado exitosamente.");
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

    // Ahora sí usamos PUT porque queremos hacer una actualización con mayor magnitud (estamos modificando más de un atributo).
    @PutMapping("/modificar") // Ruta específica para modificar un Bedel.
    public ResponseEntity<Map<String, Object>> modificarBedel(@RequestBody BedelDTO bedelDTO) {

        Map<String, Object> response = new HashMap<>();

        try {

            gestorDeBedeles.modificarBedel(bedelDTO);

            response.put("message", "Bedel modificado exitosamente.");
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