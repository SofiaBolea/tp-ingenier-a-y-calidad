package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Entity
@Table(name = "administradores")
public class Administrador extends Usuario {

    // CONSTRUCTORES:

    public Administrador() {
        super();
    }

}