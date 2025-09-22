package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Aula;

public interface AulaDAO extends JpaRepository<Aula, Integer> {

    List<Aula> buscarAulasHabilitadas(String tipoDeAula, int capacidad);
    List<Aula> buscarAula(int numeroAula, String tipoDeAula, int capacidad);
    Aula buscarPorId(int idAula);
    <T> T obtenerAulaPorIDyTipo(String tipo, int idAula, Class<T> tipoClase);
    void actualizarAula(Aula aula);

}
