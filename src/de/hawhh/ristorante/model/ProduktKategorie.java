package de.hawhh.ristorante.model;

import de.hawhh.ristorante.modelgenerator.ProduktHelper;

public enum ProduktKategorie {
    VORSPEISEN { public Class<Vorspeise> getCorrespondingClass() {return Vorspeise.class;}} ,
    HAUPTSPEISEN { public Class<Hauptspeise> getCorrespondingClass() {return Hauptspeise.class;}},
    DESSERT { public Class<Dessert> getCorrespondingClass() {return Dessert.class;}},
    GETRAENKE { public Class<Getraenk> getCorrespondingClass() {return Getraenk.class;}};

    public abstract Class<? extends Produkt> getCorrespondingClass();
}
