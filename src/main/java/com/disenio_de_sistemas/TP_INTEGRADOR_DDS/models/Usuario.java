package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    private String contrasenia;
    private String apellido;
    private String nombre;

    // GETTERS Y SETTERS:

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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
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

    // CONSTRUCTORES:

    public Usuario(){
    }

    public Usuario(String nombreUsuario, String contrasenia, String apellido, String nombre) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.apellido = apellido;
        this.nombre = nombre;
    }

}