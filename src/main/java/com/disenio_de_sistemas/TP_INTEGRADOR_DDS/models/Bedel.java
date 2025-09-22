package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.TurnoDeTrabajo;

@Entity
@Table(name = "bedeles")
public class Bedel extends Usuario {

    @Column(name = "turno_de_trabajo")
    private TurnoDeTrabajo turnoDeTrabajo;

    private boolean estado;

    // GETTERS Y SETTERS:

    public TurnoDeTrabajo getTurnoDeTrabajo() {
        return turnoDeTrabajo;
    }

    public void setTurnoDeTrabajo(TurnoDeTrabajo turnoDeTrabajo) {
        this.turnoDeTrabajo = turnoDeTrabajo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // CONSTRUCTORES:

    public Bedel() {
    }

    public Bedel(String nombreUsuario, String contrasenia, String apellido, String nombre, TurnoDeTrabajo turnoDeTrabajo) {
        super(nombreUsuario, contrasenia, apellido, nombre);
        this.turnoDeTrabajo = turnoDeTrabajo;
        estado = true; // Cada bedel nuevo registrado, en principio está en estado 'true', equivalente a estado 'activo'.
    }
}