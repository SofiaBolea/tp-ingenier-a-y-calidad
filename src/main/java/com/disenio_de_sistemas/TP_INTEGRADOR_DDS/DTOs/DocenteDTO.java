package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;



public class DocenteDTO {

    private int idDocente;
    private String docente;
    private String direccionCorreoElectronico;

    // GETTERS Y SETTERS:

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

    public String getDireccionCorreoElectronico() {
        return direccionCorreoElectronico;
    }

    public void setDireccionCorreoElectronico(String direccionCorreoElectronico) {
        this.direccionCorreoElectronico = direccionCorreoElectronico;
    }

    // CONSTRUCTORES:

    public DocenteDTO() {
    }

    public DocenteDTO(int idDocente, String docente, String direccionCorreoElectronico) {
        this.idDocente = idDocente;
        this.docente = docente;
        this.direccionCorreoElectronico = direccionCorreoElectronico;
    }
}