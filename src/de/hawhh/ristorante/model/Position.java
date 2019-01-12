package de.hawhh.ristorante.model;

import java.util.Objects;

public class Position {

    private final Produkt produkt;
    private final int anzahl;

    public Produkt getProduk() {
        return produkt;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public Position(Produkt produkt, int anzahl){
        this.produkt = produkt;
        this.anzahl = anzahl;
    }

    public int getPreis() {
        return produkt.getPreis()*anzahl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return anzahl == position.anzahl &&
                Objects.equals(produkt, position.produkt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produkt, anzahl);
    }

    @Override
    public String toString() {
        return  produkt +
                " ["  + anzahl +
                "]";
    }
}
