package de.hawhh.ristorante.modelgenerator;

import de.hawhh.ristorante.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class ProduktHelper {

    static final List<Produkt> SPEISEN = Collections.unmodifiableList(
            new ArrayList<>(new HashSet<>(Arrays.asList(new Hauptspeise("Saltimboca",17),new Vorspeise("Vitello tonnato",15),new Dessert("Marsala-Zabaione",6),new Hauptspeise("Scallopine al limone",27),new Dessert("Panna cotta",7),new Hauptspeise("Pizza Margerita",12),new Vorspeise("Cozze al marinara",16),new Vorspeise("Insalata mista",8),new Dessert("Tirami su",8),new Hauptspeise("Riso col nero di seppie",16),new Hauptspeise("Ossobuco",25),new Vorspeise("Insalata Caprese",10),new Hauptspeise("Orechiette pugliesi con salsiccia toscana piccante",17),new Hauptspeise("Penne arrabiata",15),new Vorspeise("Antipasto misto",15),new Hauptspeise("Cappon magro",28),new Hauptspeise("Arrosto di manzo",25),new Vorspeise("Insalata frutti di mare",15)))));

    static final List<Produkt> GETRAENKE = Collections.unmodifiableList(new ArrayList<>(new HashSet<>(Arrays.asList(new Getraenk("Cappucino",4),new Getraenk("Espresso",4),new Getraenk("Te nero",4),new Getraenk("Aqua",10),new Getraenk("Spuma",4),new Getraenk("Coca",3)))));

    public static List<Produkt> get(Class<? extends Produkt> clazz) {
        List<Produkt> alle = new ArrayList<>(SPEISEN);
        alle.addAll(GETRAENKE);
        return alle.stream().filter(p -> p.getClass() == clazz).collect(Collectors.toList());
    }
}
