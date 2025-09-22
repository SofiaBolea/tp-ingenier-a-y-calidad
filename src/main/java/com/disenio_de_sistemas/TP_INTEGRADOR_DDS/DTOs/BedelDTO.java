package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.TurnoDeTrabajo;

public class BedelDTO {

    private int idBedel;
    private String apellido;
    private String nombre;
    private TurnoDeTrabajo turnoDeTrabajo;
    private String nombreUsuario;
    private String contrasenia;
    private String confirmacionContrasenia;
    private boolean estado;

    // GETTERS Y SETTERS:

    public int getIdBedel() {
        return idBedel;
    }

    public void setIdBedel(int idBedel) {
        this.idBedel = idBedel;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TurnoDeTrabajo getTurnoDeTrabajo() {
        return turnoDeTrabajo;
    }

    public void setTurnoDeTrabajo(TurnoDeTrabajo turnoDeTrabajo) {
        this.turnoDeTrabajo = turnoDeTrabajo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getConfirmacionContrasenia() {
        return confirmacionContrasenia;
    }

    public void setConfirmacionContrasenia(String confirmacionContrasenia) {
        this.confirmacionContrasenia = confirmacionContrasenia;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // CONSTRUCTORES

    public BedelDTO() {
    }

    public BedelDTO(int idBedel, String apellido, String nombre, TurnoDeTrabajo turnoDeTrabajo, String nombreUsuario, String contrasenia, String confirmacionContrasenia, boolean estado) {
        this.idBedel = idBedel;
        this.apellido = apellido;
        this.nombre = nombre;
        this.turnoDeTrabajo = turnoDeTrabajo;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.confirmacionContrasenia = confirmacionContrasenia;
        this.estado = estado;
    }

}