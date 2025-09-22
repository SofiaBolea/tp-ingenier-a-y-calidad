package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "reservas_periodicas")
public class ReservaPeriodica extends Reserva {

    @ManyToMany
    @JoinTable(
            name = "reservada_en", // Tabla intermedia
            joinColumns = @JoinColumn(name = "id_reserva"), // FK a la tabla ReservaPeriodica.
            inverseJoinColumns = @JoinColumn(name = "id_periodo") // FK a la tabla Periodo.
    )
    private List<Periodo> periodos;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_reserva")
    private List<RenglonReservaPeriodica> renglonReservaPeriodica = new ArrayList<>();

    // GETTERS Y SETTERS:

    public List<Periodo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Periodo> periodos) {
        if (periodos.isEmpty() || periodos.size() > 2) {
            throw new IllegalArgumentException("Debe haber entre 1 y 2 periodos.");
        }
        this.periodos = periodos;
    }

    public List<RenglonReservaPeriodica> getRenglonReservaPeriodica() {
        return renglonReservaPeriodica;
    }

    public void setRenglonReservaPeriodica(ArrayList<RenglonReservaPeriodica> renglonReservaPeriodica) {
        this.renglonReservaPeriodica = renglonReservaPeriodica;
    }

    // CONSTRUCTORES:

    public ReservaPeriodica(){
    }

    public ReservaPeriodica(List<Periodo> periodos, ArrayList<RenglonReservaPeriodica> renglonReservaPeriodica) {
        this.periodos = periodos;
        this.renglonReservaPeriodica = renglonReservaPeriodica;
    }

    public ReservaPeriodica(int idReserva, int cantidadAlumnos, int idDocente, String docente, String correoElectronico, int idCatedra, String nombreCatedra, Bedel bedel, List<Periodo> periodos, ArrayList<RenglonReservaPeriodica> renglonReservaPeriodica) {
        super(idReserva, cantidadAlumnos, idDocente, docente, correoElectronico, idCatedra, nombreCatedra, bedel);
        this.periodos = periodos;
        this.renglonReservaPeriodica = renglonReservaPeriodica;
    }

}