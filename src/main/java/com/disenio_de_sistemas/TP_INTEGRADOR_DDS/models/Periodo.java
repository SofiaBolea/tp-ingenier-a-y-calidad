package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.sql.Date;
import jakarta.persistence.*;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.TipoDePeriodo;

@Entity
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private int idPeriodo;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_finalizacion")
    private Date fechaFinalizacion;

    @Column(name = "tipo_de_periodo")
    private TipoDePeriodo tipoDePeriodo;

    // GETTERS Y SETTERS:

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public TipoDePeriodo getTipoDePeriodo() {
        return tipoDePeriodo;
    }

    public void setTipoDePeriodo(TipoDePeriodo tipoDePeriodo) {
        this.tipoDePeriodo = tipoDePeriodo;
    }

    // CONSTRUCTORES:

    public Periodo(){
    }

    public Periodo(int idPeriodo, Date fechaInicio, Date fechaFinalizacion, TipoDePeriodo tipoDePeriodo) {
        this.idPeriodo = idPeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.tipoDePeriodo = tipoDePeriodo;
    }

}