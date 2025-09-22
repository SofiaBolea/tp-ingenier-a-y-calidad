package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs;

public class CatedraDTO {

    private int idCatedra;
    private String nombreCatedra;

    // GETTERS Y SETTERS:

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


    // CONSTRUCTORES:

    public CatedraDTO() {
    }

    public CatedraDTO(int idCatedra, String nombreCatedra) {
        this.idCatedra = idCatedra;
        this.nombreCatedra = nombreCatedra;
    }
}
