package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.time.LocalTime;
import jakarta.persistence.*;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.DiaDeSemana;

@Entity
@Table(name = "renglones_reserva_periodica")
public class RenglonReservaPeriodica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRenglonReservaPeriodica;

    private DiaDeSemana diaSemanal;
    private LocalTime horarioDeInicio;
    private int duracion;

    @ManyToOne
    @JoinColumn(name = "id_aula")
    private Aula aula;

    // GETTERS Y SETTERS:

    public int getIdRenglonReservaPeriodica() {
        return idRenglonReservaPeriodica;
    }

    public void setIdRenglonReservaPeriodica(int idRenglonReservaPeriodica) {
        this.idRenglonReservaPeriodica = idRenglonReservaPeriodica;
    }

    public DiaDeSemana getDiaSemanal() {
        return diaSemanal;
    }

    public void setDiaSemanal(DiaDeSemana diaSemanal) {
        this.diaSemanal = diaSemanal;
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

    public RenglonReservaPeriodica(){
    }

    public RenglonReservaPeriodica(int idRenglonReservaPeriodica, DiaDeSemana diaSemanal, LocalTime horarioDeInicio, int duracion, Aula aula) {
        this.idRenglonReservaPeriodica = idRenglonReservaPeriodica;
        this.diaSemanal = diaSemanal;
        this.horarioDeInicio = horarioDeInicio;
        this.duracion = duracion;
        this.aula = aula;
    }
}