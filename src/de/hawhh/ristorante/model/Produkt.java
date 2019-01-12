package de.hawhh.ristorante.model;

import java.util.*;

public class Produkt {

    private final String name;
    private final int preis;

    public Produkt(String name, int preis) {
       this.name = name;
       this.preis = preis;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Produkt)) return false;
        Produkt produkt = (Produkt) o;
        return preis == produkt.preis &&
                Objects.equals(name, produkt.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, preis);
    }

    @Override
    public String toString() {
        return name+ " "+ preis +"(€)";
    }

    public int getPreis() {
        return preis;
    }
    public String getName() {return name;}
}
