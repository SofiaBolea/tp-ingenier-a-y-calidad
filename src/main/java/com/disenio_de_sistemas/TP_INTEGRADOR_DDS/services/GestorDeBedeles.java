package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Bedel;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.BedelDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.BedelDAO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices.ValidadorRegistroBedel;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices.ValidadorModificacionBedel;

@Service
public class GestorDeBedeles {

    private final BedelDAO bedelDAO;
    private final ValidadorRegistroBedel validadorRegistroBedel;
    private final ValidadorModificacionBedel validadorModificacionBedel;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias:
    @Autowired
    public GestorDeBedeles(BedelDAO bedelDAO, ValidadorRegistroBedel validadorRegistroBedel, PasswordEncoder passwordEncoder, ValidadorModificacionBedel validadorModificacionBedel) {
        this.bedelDAO = bedelDAO;
        this.validadorRegistroBedel = validadorRegistroBedel;
        this.validadorModificacionBedel = validadorModificacionBedel;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo para registrar un nuevo bedel en la base de datos:
    public void registrarBedel(BedelDTO bedelDTO) {

        // Encapsulamos la lógica de validación de datos de entrada dentro del mismo metodo de registro de bedel:
        try {
            validadorRegistroBedel.validarBedelDTO(bedelDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        // Creamos la entidad y le asignamos los respectivos datos:
        Bedel bedel = new Bedel();

        bedel.setNombreUsuario(bedelDTO.getNombreUsuario());

        // Hasheamos la contraseña antes de almacenarla para protegerla y asegurarse de que nunca se almacenará en formato de texto sin cifrar (que sería legible) en la base de datos.
        String hashedPassword = passwordEncoder.encode(bedelDTO.getContrasenia());
        bedel.setContrasenia(hashedPassword);

        bedel.setApellido(bedelDTO.getApellido());
        bedel.setNombre(bedelDTO.getNombre());
        bedel.setTurnoDeTrabajo(bedelDTO.getTurnoDeTrabajo());
        bedel.setEstado(bedelDTO.getEstado());

        // Invocamos el metodo para guardar el Bedel en la base de datos (sólo si éste no existe ya):
        if (bedelDAO.findByNombreUsuario(bedelDTO.getNombreUsuario()) == null) {
            bedelDAO.save(bedel);
        } else {
            throw new IllegalArgumentException("Ya existe un usuario activo en el sistema con ese identificador.");
        }
    }

    // Metodo usado para buscar los bedeles que coincidan con los filtros de búsqueda ingresados:
    public List<BedelDTO> buscarBedel(BedelDTO bedelDTO) {

        List<Bedel> bedelesEncontrados = bedelDAO.findByApellidoOrTurnoDeTrabajo(bedelDTO.getApellido(), bedelDTO.getTurnoDeTrabajo());

        // Convertimos todas las entidades de bedeles encontradas a DTOs para transferirlos a la capa de presentación:
        List<BedelDTO> bedelesDTO = new ArrayList<>();

        for (Bedel bedel : bedelesEncontrados) {
            BedelDTO bedelDTOEncontrado = new BedelDTO();
            bedelDTOEncontrado.setIdBedel((bedel.getIdUsuario()));
            bedelDTOEncontrado.setNombreUsuario(bedel.getNombreUsuario());
            bedelDTOEncontrado.setApellido(bedel.getApellido());
            bedelDTOEncontrado.setNombre(bedel.getNombre());
            bedelDTOEncontrado.setTurnoDeTrabajo(bedel.getTurnoDeTrabajo());
            bedelDTOEncontrado.setEstado(bedel.getEstado());
            bedelesDTO.add(bedelDTOEncontrado);
        }

        return bedelesDTO;
    }

    // Metodo para obtener una entidad bedel por su nombre de usuario en la base de datos:
    public Bedel obtenerBedelPorNombreUsuario(String nombreUsuario){
        return bedelDAO.findByNombreUsuario(nombreUsuario);
    }

    // Metodo para contar el total de bedeles ACTIVOS en la base de datos (para poder mostrar en la interfaz: 'X bedeles encontrados de Y totales').
    public long contarTotalBedeles() {
        return bedelDAO.totalBedelesEnBD();
    }

    // Metodo para realizar la eliminación LÓGICA de un bedel (estado ACTIVO -> INACTIVO):
    public void eliminarBedel(BedelDTO bedelDTO) {

        // Buscamos el bedel que se quiere eliminar por su NOMBRE DE USUARIO (no por su ID):
        Bedel bedel = bedelDAO.findByNombreUsuario(bedelDTO.getNombreUsuario());

        // Si bien no es necesario validar la existencia, ya que éste metodo es una extensión del metodo buscarBedel y es ese es el que se encarga de de validar la existencia, agregamos una validación adicional por si se intenta ejecutar el metodo eliminarBedel de forma independie (llamando al endpoint).
        if (bedel == null) {
            throw new IllegalArgumentException("No se encontró un bedel con el nombre de usuario especificado: " + bedelDTO.getNombreUsuario());
        }

        bedel.setEstado(false);

        bedelDAO.actualizarBedel(bedel); // Persistimos el cambio.
    }

    // Metodo para modificar los datos de un bedel exsistente en la base de datos:
    public void modificarBedel(BedelDTO bedelDTO) {

        // Encapsulamos la lógica de validación de datos de entrada dentro del mismo metodo de modificacion de bedel:
        try {
            validadorModificacionBedel.validarBedelDTO(bedelDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        // Si se valida correctamente el formato de los datos a modificar, buscamos el bedel que se quiere modificar por su NOMBRE DE USUARIO (no por su ID):
        Bedel bedel = bedelDAO.findByNombreUsuario(bedelDTO.getNombreUsuario());

        // Si bien no es necesario validar la existencia, ya que éste metodo es una extensión del metodo buscarBedel y es ese es el que se encarga de de validar la existencia, agregamos una validación adicional por si se intenta ejecutar el metodo eliminarBedel de forma independiente (llamando al endpoint).
        if (bedel == null) {
            throw new IllegalArgumentException("No se encontró un bedel con el nombre de usuario especificado: " + bedelDTO.getNombreUsuario());
        }

        // Hasheamos la contraseña nueva (si es que se modificó) antes de almacenarla para protegerla y asegurarse de que nunca se almacenará en formato de texto sin cifrar (que sería legible) en la base de datos.
        if (bedelDTO.getContrasenia() != null && !passwordEncoder.matches(bedelDTO.getContrasenia(), bedel.getContrasenia())) {
            String hashedPassword = passwordEncoder.encode(bedelDTO.getContrasenia());
            bedel.setContrasenia(hashedPassword);
        }

        bedel.setApellido(bedelDTO.getApellido());
        bedel.setNombre(bedelDTO.getNombre());
        bedel.setTurnoDeTrabajo(bedelDTO.getTurnoDeTrabajo());

        bedelDAO.actualizarBedel(bedel); // Persistimos los cambios.
    }

}