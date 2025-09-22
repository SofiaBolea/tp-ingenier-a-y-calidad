package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "reservas")
public abstract class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private int idReserva;

    @Column(name = "cantidad_alumnos")
    private int cantidadAlumnos;

    @Column(name = "id_docente", nullable = false)
    private int idDocente;

    @Transient
    private String docente;

    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico; // A este atributo sí lo ponemos en la tabla de reservas porque es el correo electrónico de contacto (no necesariamente el del docente solicitante).

    @Column(name = "id_catedra", nullable = false)
    private int idCatedra;

    @Transient
    private String nombreCatedra;

    @Column(name = "fecha_reservacion", nullable = false)
    private LocalDate fechaReservacion;

    // Relación unidireccional con Bedel:
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Bedel bedel;

    // GETTERS Y SETTERS:

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getCantidadAlumnos() {
        return cantidadAlumnos;
    }

    public void setCantidadAlumnos(int cantidadAlumnos) {
        this.cantidadAlumnos = cantidadAlumnos;
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

    public void setFechaReservacion() {
        this.fechaReservacion = LocalDate.now();
    }

    public Bedel getBedel() {
        return bedel;
    }

    public void setBedel(Bedel bedel) {
        this.bedel = bedel;
    }

    // CONSTRUCTORES:

    public Reserva(){
    }

    public Reserva(int idReserva, int cantidadAlumnos, int idDocente, String docente, String correoElectronico, int idCatedra, String nombreCatedra, Bedel bedel) {
        this.idReserva = idReserva;
        this.cantidadAlumnos = cantidadAlumnos;
        this.idDocente = idDocente;
        this.docente = docente;
        this.correoElectronico = correoElectronico;
        this.idCatedra = idCatedra;
        this.nombreCatedra = nombreCatedra;
        this.fechaReservacion = LocalDate.now();
        this.bedel = bedel;
    }
}