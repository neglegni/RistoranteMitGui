package de.hawhh.ristorante.model;

public enum Tisch {
    T1("Tisch 1"), T2("Tisch 2"), T3("Tisch 3"), T4("Tisch 4"), T5("Tisch 5");

    private final String name;

    Tisch(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
