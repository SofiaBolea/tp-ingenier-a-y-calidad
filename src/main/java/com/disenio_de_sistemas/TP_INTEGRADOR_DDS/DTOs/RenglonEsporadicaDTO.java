package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class RenglonEsporadicaDTO {

    private LocalDate fecha;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private int duracion;
    private String nombreAula;
    private int idAula;

    // GETTERS Y SETTERS:

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(LocalTime horarioFin) {
        this.horarioFin = horarioFin;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getNombreAula() {
        return nombreAula;
    }

    public void setNombreAula(String nombreAula) {
        this.nombreAula = nombreAula;
    }

    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    // CONSTRUCTORES:

    public RenglonEsporadicaDTO() {
    }

    public RenglonEsporadicaDTO(LocalDate fecha, LocalTime horarioInicio, LocalTime horarioFin, int duracion, String nombreAula, int idAula) {
        this.fecha = fecha;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.duracion = duracion;
        this.nombreAula = nombreAula;
        this.idAula = idAula;
    }
}