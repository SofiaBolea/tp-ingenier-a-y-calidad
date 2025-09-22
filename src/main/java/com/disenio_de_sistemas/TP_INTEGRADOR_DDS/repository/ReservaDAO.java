package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository;

import java.util.Map;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.*;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.DiaDeSemana;

public interface ReservaDAO extends JpaRepository<Reserva, Integer> {

    // Metodos para obtener disponibilidad de aulas:
    <T extends Aula> List<T> obtenerDisponibilidadEsporadica(List<Integer> idsAulas,LocalDate fechaRenglon,LocalTime horaInicio,int duracion,Class<T> tipoAula);
    <T extends Aula> List<T> obtenerDisponibilidadPeriodica(List<Integer> idsAulas, List<Integer> idPeriodo, DiaDeSemana diaSemanal, LocalTime horaInicio, int duracion, Class<T> tipoAula);

    // Metodos para obtener las reservas que generan superposición (en el caso de no obtener disponibilidad):
    List<RenglonReservaEsporadica> obtenerSuperposicionesEsporadicaEsporadicas(List<Integer> idsAulas, LocalDate fechaRenglon, LocalTime horaInicio, LocalTime horaFin);
    List<RenglonReservaPeriodica> obtenerSuperposicionesEsporadicaPeriodicas(List<Integer> idsAulas, LocalDate fechaRenglon, LocalTime horaInicio, LocalTime horaFin);
    List<RenglonReservaEsporadica> obtenerSuperposicionesPeriodicaEsporadicas(List<Integer> idsAulas, DiaDeSemana diaSemanal, LocalTime horaInicio, LocalTime horaFin);
    List<RenglonReservaPeriodica> obtenerSuperposicionesPeriodicaPeriodicas(List<Integer> idsAulas, DiaDeSemana diaSemanal, LocalTime horaInicio, LocalTime horaFin);

    // Metodos complementarios:
    int obtenerIdReservaPorIdRenglonEsporadico(int idRenglon);
    int obtenerIdReservaPorIdRenglonPeriodico(int idRenglon);
    Map<String, Object> obtenerReservaPorId(int idReserva);
    Periodo buscarPeriodoPorId(int idPeriodo);

}