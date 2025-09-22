package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Define cómo mapear la herencia en la base de datos.
@Table(name = "aulas")
public abstract class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private int id_aula;

    private String nombre_aula;
    private int piso;
    private int capacidad;
    private boolean estado;
    private boolean aire_acondicionado;

    @ManyToMany
    @JoinTable(
            name = "contiene", // Tabla intermedia.
            joinColumns = @JoinColumn(name = "id_aula"), // FK a la tabla aulas.
            inverseJoinColumns = @JoinColumn(name = "id_tipo") // FK a la tabla pizarrones.
    )
    private List<TipoDePizarron> tipoDePizarrones = new ArrayList<>();

    // GETTERS Y SETTERS:

    public int getIdAula() {
        return id_aula;
    }

    public void setIdAula(int id_aula) {
        this.id_aula = id_aula;
    }

    public String getNombreAula() {
        return nombre_aula;
    }

    public void setNombreAula(String nombre_aula) {
        this.nombre_aula = nombre_aula;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean getAireAcondicionado() {
        return aire_acondicionado;
    }

    public void setAireAcondicionado(boolean aire_acondicionado) {
        this.aire_acondicionado = aire_acondicionado;
    }

    public List<TipoDePizarron> getTipoDePizarrones() {
        return tipoDePizarrones;
    }

    public void setTipoDePizarrones(List<TipoDePizarron> tipoDePizarrones) {

        this.tipoDePizarrones = tipoDePizarrones;
    }

    // CONSTRUCTORES:

    public Aula(){
    }

    public Aula(int id_aula, String nombre_aula, int piso, int capacidad, boolean estado, boolean aire_acondicionado, ArrayList<TipoDePizarron> tipoDePizarrones) {
        this.id_aula = id_aula;
        this.nombre_aula = nombre_aula;
        this.piso = piso;
        this.capacidad = capacidad;
        this.estado = estado;
        this.aire_acondicionado = aire_acondicionado;
        this.tipoDePizarrones = tipoDePizarrones;
    }
}