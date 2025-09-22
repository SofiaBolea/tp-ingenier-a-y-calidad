package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.util.ArrayList;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Entity
@Table(name = "aulas_SRA")
public class AulaSinRecursosAdicionales extends Aula {

    private boolean ventiladores;

    // GETTERS Y SETTERS:

    public boolean getVentiladores() {
        return ventiladores;
    }

    public void setVentiladores(boolean ventiladores) {
        this.ventiladores = ventiladores;
    }

    // CONSTRUCTORES:

    public AulaSinRecursosAdicionales() {
    }

    public AulaSinRecursosAdicionales(boolean ventiladores) {
        this.ventiladores = ventiladores;
    }

    public AulaSinRecursosAdicionales(int idAula, String nombreAula, int piso, int capacidad, boolean estado, boolean aireAcondicionado, ArrayList<TipoDePizarron> tipoDePizarrones, boolean ventiladores) {
        super(idAula, nombreAula, piso, capacidad, estado, aireAcondicionado, tipoDePizarrones);
        this.ventiladores = ventiladores;
    }
}