package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.util.ArrayList;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Entity
@Table(name = "aulas_informatica")
public class AulaInformatica extends Aula {

    private boolean canion;
    private int cantidadComputadoras;

    // GETTERS Y SETTERS:

    public boolean getCanion() {
        return canion;
    }

    public void setCanion(boolean canion) {
        this.canion = canion;
    }

    public int getCantidadComputadora() {
        return cantidadComputadoras;
    }

    public void setCantidadComputadora(int cantidadComputadora) {
        this.cantidadComputadoras = cantidadComputadora;
    }

    // CONSTRUCTORES:

    public AulaInformatica() {
    }

    public AulaInformatica(boolean canion, int cantidadComputadoras) {
        this.canion = canion;
        this.cantidadComputadoras = cantidadComputadoras;
    }

    public AulaInformatica(int idAula, String nombreAula, int piso, int capacidad, boolean estado, boolean aireAcondicionado, ArrayList<TipoDePizarron> tipoDePizarrones, boolean canion, int cantidadComputadoras) {
        super(idAula, nombreAula, piso, capacidad, estado, aireAcondicionado, tipoDePizarrones);
        this.canion = canion;
        this.cantidadComputadoras = cantidadComputadoras;
    }
}