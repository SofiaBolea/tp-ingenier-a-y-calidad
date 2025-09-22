package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity
@Table(name = "reservas_esporadicas")
public class ReservaEsporadica extends Reserva {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_reserva")
    private List<RenglonReservaEsporadica> renglonReservaEsporadica = new ArrayList<>();

    // GETTERS Y SETTERS:

    public List<RenglonReservaEsporadica> getRenglonReservaEsporadica() {
        return renglonReservaEsporadica;
    }

    public void setRenglonReservaEsporadica(ArrayList<RenglonReservaEsporadica> renglonReservaEsporadica) {
        this.renglonReservaEsporadica = renglonReservaEsporadica;
    }

    // CONSTRUCTORES:

    public ReservaEsporadica(){
    }

    public ReservaEsporadica(ArrayList<RenglonReservaEsporadica> renglonReservaEsporadica) {
        this.renglonReservaEsporadica = renglonReservaEsporadica;
    }

    public ReservaEsporadica(int idReserva, int cantidadAlumnos, int idDocente, String docente, String correoElectronico, int idCatedra, String nombreCatedra, Bedel bedel, ArrayList<RenglonReservaEsporadica> renglonReservaEsporadica) {
        super(idReserva, cantidadAlumnos, idDocente, docente, correoElectronico, idCatedra, nombreCatedra, bedel);
        this.renglonReservaEsporadica = renglonReservaEsporadica;
    }

}