package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Bedel;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.TurnoDeTrabajo;

public interface BedelDAO extends JpaRepository<Bedel, Integer> {
    
    Bedel findByNombreUsuario(String nombreUsuario);
    List<Bedel> findByApellidoOrTurnoDeTrabajo(String apellido, TurnoDeTrabajo turnoDeTrabajo);
    long totalBedelesEnBD();
    void actualizarBedel(Bedel bedel);

}
