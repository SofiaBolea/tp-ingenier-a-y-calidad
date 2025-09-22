package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;

import java.util.List;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"tipoReserva", "fechasEsporadicas", "diasSeleccionados", "docente", "nombreCatedra", "correoElectronico", "idUsuario", "iddocente", "idcatedra", "idperiodos", "cantidadAlumnos", "tipoDeAula", "fechaReservacion"}) // Es irrelevante, sólo modifica el orden de los atributos de esta clase cuando se convierte a JSON para mostrar en la interfaz.
public class ReservaDTO {

    // Página 1 - Dato general de la reserva:
    private String tipoReserva; // "PERIODICA" o "ESPORADICA"

    // Para reservas esporádicas:
    private List<RenglonEsporadicaDTO> fechasEsporadicas; // Detalles por fecha seleccionada.

    // Para reservas periódicas:
    private List<Integer> idPeriodos;

    private List<RenglonPeriodicaDTO> diasSeleccionados; // Detalles por día seleccionado.

    // Página 2 - Datos generales de la reserva:
    private int idUsuario;
    private String nombreUsuario;
    private int cantidadAlumnos;
    private String tipoDeAula;
    private int idDocente;
    private String docente;
    private int idCatedra;
    private String nombreCatedra;
    private String correoElectronico;
    private LocalDate fechaReservacion;

    // GETTERS Y SETTERS:

    public String getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(String tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public List<RenglonEsporadicaDTO> getFechasEsporadicas() {
        return fechasEsporadicas;
    }

    public void setFechasEsporadicas(List<RenglonEsporadicaDTO> fechasEsporadicas) {
        this.fechasEsporadicas = fechasEsporadicas;
    }

    public List<Integer> getIdPeriodos() {
        return idPeriodos;
    }

    public void setIdPeriodos(List<Integer> idPeriodos) {
        this.idPeriodos = idPeriodos;
    }

    public List<RenglonPeriodicaDTO> getDiasSeleccionados() {
        return diasSeleccionados;
    }

    public void setDiasSeleccionados(List<RenglonPeriodicaDTO> diasSeleccionados) {
        this.diasSeleccionados = diasSeleccionados;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getCantidadAlumnos() {
        return cantidadAlumnos;
    }

    public void setCantidadAlumnos(int cantidadAlumnos) {
        this.cantidadAlumnos = cantidadAlumnos;
    }

    public String getTipoDeAula() {
        return tipoDeAula;
    }

    public void setTipoDeAula(String tipoDeAula) {
        this.tipoDeAula = tipoDeAula;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public int getIdCatedra() {
        return idCatedra;
    }

    public void setIdCatedra(int idCatedra) {
        this.idCatedra = idCatedra;
    }

    public String getNombreCatedra() {
        return nombreCatedra;
    }

    public void setNombreCatedra(String nombreCatedra) {
        this.nombreCatedra = nombreCatedra;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public LocalDate getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(LocalDate fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    // CONSTRUCTORES:

    public ReservaDTO(){
    }

    // Para reserva periódica:
    public ReservaDTO(String tipoReserva, List<Integer> idPeriodos, List<RenglonPeriodicaDTO> diasSeleccionados, int idUsuario, String nombreUsuario, int cantidadAlumnos, String tipoDeAula, int idDocente, String docente, int idCatedra, String nombreCatedra, String correoElectronico, LocalDate fechaReservacion) {
        this.tipoReserva = tipoReserva;
        this.idPeriodos = idPeriodos;
        this.diasSeleccionados = diasSeleccionados;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.cantidadAlumnos = cantidadAlumnos;
        this.tipoDeAula = tipoDeAula;
        this.idDocente = idDocente;
        this.docente = docente;
        this.idCatedra = idCatedra;
        this.nombreCatedra = nombreCatedra;
        this.correoElectronico = correoElectronico;
        this.fechaReservacion = fechaReservacion;
    }

    // Para reserva esporádica:


    public ReservaDTO(String tipoReserva, List<RenglonEsporadicaDTO> fechasEsporadicas, int idUsuario, String nombreUsuario, int cantidadAlumnos, String tipoDeAula, int idDocente, String docente, int idCatedra, String nombreCatedra, String correoElectronico, LocalDate fechaReservacion) {
        this.tipoReserva = tipoReserva;
        this.fechasEsporadicas = fechasEsporadicas;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.cantidadAlumnos = cantidadAlumnos;
        this.tipoDeAula = tipoDeAula;
        this.idDocente = idDocente;
        this.docente = docente;
        this.idCatedra = idCatedra;
        this.nombreCatedra = nombreCatedra;
        this.correoElectronico = correoElectronico;
        this.fechaReservacion = fechaReservacion;
    }
}