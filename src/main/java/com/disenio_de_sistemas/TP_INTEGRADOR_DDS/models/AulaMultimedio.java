package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.util.ArrayList;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Entity
@Table(name = "aulas_multimedio")
public class AulaMultimedio extends Aula {

    private boolean televisor;
    private boolean canion;
    private boolean computadora;
    private boolean ventiladores;

    // GETTERS Y SETTERS:

    public boolean getTelevisor() {
        return televisor;
    }

    public void setTelevisor(boolean televisor) {
        this.televisor = televisor;
    }

    public boolean getCanion() {
        return canion;
    }

    public void setCanion(boolean canion) {
        this.canion = canion;
    }

    public boolean getComputadora() {
        return computadora;
    }

    public void setComputadora(boolean computadora) {
        this.computadora = computadora;
    }

    public boolean getVentiladores() {
        return ventiladores;
    }

    public void setVentiladores(boolean ventiladores) {
        this.ventiladores = ventiladores;
    }

    // CONSTRUCTORES:

    public AulaMultimedio() {
    }

    public AulaMultimedio(boolean televisor, boolean canion, boolean computadora, boolean ventiladores) {
        this.televisor = televisor;
        this.canion = canion;
        this.computadora = computadora;
        this.ventiladores = ventiladores;
    }

    public AulaMultimedio(int idAula, String nombreAula, int piso, int capacidad, boolean estado, boolean aireAcondicionado, ArrayList<TipoDePizarron> tipoDePizarrones, boolean televisor, boolean canion, boolean computadora, boolean ventiladores) {
        super(idAula, nombreAula, piso, capacidad, estado, aireAcondicionado, tipoDePizarrones);
        this.televisor = televisor;
        this.canion = canion;
        this.computadora = computadora;
        this.ventiladores = ventiladores;
    }
}