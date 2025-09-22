package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pizarrones")

public class TipoDePizarron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private int idTipo;

    private String tipo;

    // GETTERS Y SETTERS:

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // CONSTRUCTORES:

    public TipoDePizarron() {
    }

    public TipoDePizarron(int idTipo, String tipo) {
        this.idTipo = idTipo;
        this.tipo = tipo;
    }
}