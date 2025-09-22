package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@Table(name = "renglones_reserva_esporadica")
public class RenglonReservaEsporadica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRenglonReservaEsporadica;

    private LocalDate fecha;
    private LocalTime horarioDeInicio;
    private int duracion;

    @ManyToOne
    @JoinColumn(name = "id_aula")
    private Aula aula;

    // GETTERS Y SETTERS:

    public int getIdRenglonReservaEsporadica() {
        return idRenglonReservaEsporadica;
    }

    public void setIdRenglonReservaEsporadica(int idRenglonReservaEsporadica) {
        this.idRenglonReservaEsporadica = idRenglonReservaEsporadica;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHorarioDeInicio() {
        return horarioDeInicio;
    }

    public void setHorarioDeInicio(LocalTime horarioDeInicio) {
        this.horarioDeInicio = horarioDeInicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    // CONSTRUCTORES:

    public RenglonReservaEsporadica() {

    }

    public RenglonReservaEsporadica(int idRenglonReservaEsporadica, LocalDate fecha, LocalTime horarioDeInicio, int duracion, Aula aula) {
        this.idRenglonReservaEsporadica = idRenglonReservaEsporadica;
        this.fecha = fecha;
        this.horarioDeInicio = horarioDeInicio;
        this.duracion = duracion;
        this.aula = aula;
    }

}