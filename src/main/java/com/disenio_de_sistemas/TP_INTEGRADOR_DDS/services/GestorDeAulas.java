package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Aula;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.AulaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.AulaDAO;

@Service
public class GestorDeAulas {

    private final AulaDAO aulaDAO;

    // Inyección de dependencia:
    @Autowired
    public GestorDeAulas(AulaDAO aulaDAO) {
        this.aulaDAO = aulaDAO;
    }

    // Metodo usado para buscar las aulas habilitadas (estado TRUE) que cumplan con el criterio de capacidad y tipo al momento de registrar una reserva:
    public List<AulaDTO> buscarAulasHabilitadas(String tipoDeAula, int capacidad){

        List<Aula> aulasEncontradas = aulaDAO.buscarAulasHabilitadas(tipoDeAula, capacidad);

        // Convertimos todas las entidades de aulas encontradas a DTOs para transferirlas a la capa de presentación:
        List<AulaDTO> aulasDTO = new ArrayList<>();

        for (Aula aula : aulasEncontradas) {
            AulaDTO aulaDTOEncontrada = new AulaDTO();
            aulaDTOEncontrada.setIdAula((aula.getIdAula())); // Corresponde a número de aula.
            aulaDTOEncontrada.setTipoDeAula(aula.getClass().getSimpleName());
            aulaDTOEncontrada.setCapacidad(aula.getCapacidad());

            aulasDTO.add(aulaDTOEncontrada);
        }

        return aulasDTO;
    }

    // Metodo usado para buscar las aulas que coincidan con los filtros de búsqueda ingresados:
    public List<AulaDTO> buscarAula(AulaDTO aulaDTO){

        List<Aula> aulasEncontradas = aulaDAO.buscarAula(aulaDTO.getIdAula(),aulaDTO.getTipoDeAula(),aulaDTO.getCapacidad());

        // Convertimos todas las entidades de aulas encontradas a DTOs para transferirlas a la capa de presentación:
        List<AulaDTO> aulasDTO = new ArrayList<>();

        for (Aula aula : aulasEncontradas) {
            AulaDTO aulaDTOEncontrada = new AulaDTO();
            aulaDTOEncontrada.setIdAula((aula.getIdAula())); // Corresponde a número de aula.
            aulaDTOEncontrada.setPiso(aula.getPiso());
            aulaDTOEncontrada.setTipoDeAula(aula.getClass().getSimpleName());
            aulaDTOEncontrada.setCapacidad(aula.getCapacidad());
            aulaDTOEncontrada.setEstado(aula.getEstado());

            aulasDTO.add(aulaDTOEncontrada);
        }

        return aulasDTO;
    }

    // Metodo para obtener una entidad aula a partir de su ID (para poder asociarla a una reserva nueva):
    public Aula obtenerAulaPorId(int idAula){
        return aulaDAO.buscarPorId(idAula);
    }

    // Metodo para obtener un aula por su tipo e ID (SÍ O SÍ ambos, no sólo ID):
    public <T> T obtenerAulaPorIDyTipo(String tipo, int idAula, Class<T> tipoClase){
        return aulaDAO.obtenerAulaPorIDyTipo(tipo,idAula,tipoClase);
    }

    // Metodo para obtener todas las aulas de la base de datos y poder mostrarlas (sus IDs) en la interfaz de usuario para seleccionar al momento de registrar una reserva:
    public List<AulaDTO> obtenerAulas() {

        // Recuperamos de la base de datos todas las entidades de Aula que existan:
        List<Aula> aulas = aulaDAO.findAll();

        // Convertimos las entidades en DTOs para transferirlas a la capa de presentación:
        List<AulaDTO> aulaDTOs = new ArrayList<>();

        for (Aula aula : aulas) {
            AulaDTO aulaDTO = new AulaDTO();
            aulaDTO.setIdAula(aula.getIdAula());
            aulaDTO.setPiso(aula.getPiso());
            aulaDTO.setCapacidad(aula.getCapacidad());
            aulaDTO.setTipoDeAula(aula.getClass().getSimpleName());
            aulaDTO.setEstado(aula.getEstado());

            aulaDTOs.add(aulaDTO);
        }

        return aulaDTOs;
    }

    // Metodo para obtener todos los TIPOS DE AULAS existentes (sin duplicados) en la base de datos:
    public List<String> obtenerTipos() {

        // Recuperamos de la base de datos todas las entidades de AULA que existan:
        List<Aula> aulas = aulaDAO.findAll();

        return aulas.stream()
                .map(aula -> aula.getClass().getSimpleName()) // Obtenemos el nombre de la clase de cada Aula.
                .distinct() // Eliminamos duplicados.
                .collect(Collectors.toList()); // Recolectamos en una lista.
    }

    // Metodo para realizar la eliminación LÓGICA de un aula (estado HABILITADO -> DESHABILITADO):
    public void eliminarAula(AulaDTO aulaDTO) {

        // Buscamos el aula que se quiere eliminar por su ID:
        List<Aula> aula = aulaDAO.buscarAula(aulaDTO.getIdAula(),null,0);


        // Si bien no es necesario validar la existencia, ya que éste metodo es una extensión del metodo buscarAula y es ese es el que se encarga de de validar la existencia, agregamos una validación adicional por si se intenta ejecutar el metodo eliminarAula de forma independie (llamando al endpoint).
        if (aula.isEmpty()) {
            throw new NoSuchElementException("No se encontró un aula con el ID especificado: " + aulaDTO.getIdAula());
        } else if(!aula.getFirst().getEstado()){
            throw new IllegalArgumentException("No se puede eliminar un aula que ya está en estado 'DESHABILITADA'.");
        }

        aula.getFirst().setEstado(false);
        // Trabajamos con 'getFirst()' ya que reutilizamos el metodo para obtener aulas desde la base de datos (pero que trabaja con listas).

        aulaDAO.actualizarAula(aula.getFirst()); // Persistimos el cambio.

    }
}