package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;

import java.time.LocalTime;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.DiaDeSemana;

public class RenglonPeriodicaDTO {
    private DiaDeSemana diaSemanal;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private int duracion;
    private String nombreAula;
    private int idAula;

    // GETTERS Y SETTERS:

    public DiaDeSemana getDiaSemanal() {
        return diaSemanal;
    }

    public void setDiaSemanal(DiaDeSemana diaSemanal) {
        this.diaSemanal = diaSemanal;
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

    public RenglonPeriodicaDTO() {
    }

    public RenglonPeriodicaDTO(DiaDeSemana diaSemanal, LocalTime horarioInicio, LocalTime horarioFin, int duracion, String nombreAula, int idAula) {
        this.diaSemanal = diaSemanal;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.duracion = duracion;
        this.nombreAula = nombreAula;
        this.idAula = idAula;
    }
}