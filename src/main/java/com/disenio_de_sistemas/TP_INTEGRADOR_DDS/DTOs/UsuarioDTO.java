package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;

public class UsuarioDTO {

    private String nombreUsuario;
    private String contrasenia;

    // GETTERS Y SETTERS:

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

    // CONSTRUCTORES:

    public UsuarioDTO() {
    }

    public UsuarioDTO(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

}