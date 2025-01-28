package model;
// Generated 15 ene 2025, 15:03:00 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Ciclos generated by hbm2java
 */
public class Ciclos implements java.io.Serializable {

    private static final long serialVersionUID = 6448776979065012089L;

    private int id;
    private String nombre;

    public Ciclos() {
    }

    public Ciclos(int id) {
        this.id = id;
    }

    public Ciclos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Ciclos [id=" + id + ", nombre=" + nombre + "]";
    }

}
