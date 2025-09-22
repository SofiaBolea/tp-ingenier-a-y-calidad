package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"idAula", "piso", "tipoDeAula", "capacidad", "estado"}) // Es irrelevante, sólo modifica el orden de los atributos de esta clase cuando se convierte a JSON para mostrar en la interfaz.
public class AulaDTO {

    // Características generales:
    private int id_aula;
    private int piso;
    private String tipo_de_aula;
    private int capacidad;
    private boolean estado;

    // Características específicas:
    private boolean aire_acondicionado;
    private boolean ventiladores;
    private boolean televisor;
    private boolean canion;
    private boolean computadoras;
    private int cant_computadoras;

    // GETTERS Y SETTERS:

    public int getIdAula() {
        return id_aula;
    }

    public void setIdAula(int id_aula) {
        this.id_aula = id_aula;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getTipoDeAula() {
        return tipo_de_aula;
    }

    public void setTipoDeAula(String tipo_de_aula) {
        this.tipo_de_aula = tipo_de_aula;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isAire_acondicionado() {
        return aire_acondicionado;
    }

    public void setAire_acondicionado(boolean aire_acondicionado) {
        this.aire_acondicionado = aire_acondicionado;
    }

    public boolean isVentiladores() {
        return ventiladores;
    }

    public void setVentiladores(boolean ventiladores) {
        this.ventiladores = ventiladores;
    }

    public boolean isTelevisor() {
        return televisor;
    }

    public void setTelevisor(boolean televisor) {
        this.televisor = televisor;
    }

    public boolean isCanion() {
        return canion;
    }

    public void setCanion(boolean canion) {
        this.canion = canion;
    }

    public boolean isComputadoras() {
        return computadoras;
    }

    public void setComputadoras(boolean computadoras) {
        this.computadoras = computadoras;
    }

    public int getCant_computadoras() {
        return cant_computadoras;
    }

    public void setCant_computadoras(int cant_computadoras) {
        this.cant_computadoras = cant_computadoras;
    }

    // CONSTRUCTORES:

    public AulaDTO(){
    }

    // Constructor básico y general:
    public AulaDTO(int id_aula, int piso, String tipo_de_aula, int capacidad, boolean estado, boolean aire_acondicionado) {
        this.id_aula = id_aula;
        this.piso = piso;
        this.tipo_de_aula = tipo_de_aula;
        this.capacidad = capacidad;
        this.estado = estado;
        this.aire_acondicionado = aire_acondicionado;
    }

    // Constructor para AulaDTO Multimedio:
    public AulaDTO(int id_aula, int piso, String tipo_de_aula, int capacidad, boolean estado, boolean aire_acondicionado, boolean ventiladores, boolean televisor, boolean canion, boolean computadoras) {
        this.id_aula = id_aula;
        this.piso = piso;
        this.tipo_de_aula = tipo_de_aula;
        this.capacidad = capacidad;
        this.estado = estado;
        this.aire_acondicionado = aire_acondicionado;
        this.ventiladores = ventiladores;
        this.televisor = televisor;
        this.canion = canion;
        this.computadoras = computadoras;
    }

    // Constructor para AulaDTO Informática:
    public AulaDTO(int id_aula, int piso, String tipo_de_aula, int capacidad, boolean estado, boolean aire_acondicionado, boolean canion, int cant_computadoras) {
        this.id_aula = id_aula;
        this.piso = piso;
        this.tipo_de_aula = tipo_de_aula;
        this.capacidad = capacidad;
        this.estado = estado;
        this.aire_acondicionado = aire_acondicionado;
        this.canion = canion;
        this.cant_computadoras = cant_computadoras;
    }

    // Constructor para AulaDTO SRA:
    public AulaDTO(int id_aula, int piso, String tipo_de_aula, int capacidad, boolean estado, boolean aire_acondicionado, boolean ventiladores) {
        this.id_aula = id_aula;
        this.piso = piso;
        this.tipo_de_aula = tipo_de_aula;
        this.capacidad = capacidad;
        this.estado = estado;
        this.aire_acondicionado = aire_acondicionado;
        this.ventiladores = ventiladores;
    }

}
