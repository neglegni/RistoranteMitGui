package de.hawhh.ristorante.modelgenerator;


import de.hawhh.ristorante.model.Position;
import de.hawhh.ristorante.model.Produkt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PositionGenerator {

    private static  final int MAX_SPEISEN = 6;
    private static  final int MAX_GETRAENKE = 9;


   public static List<Position> generierePositionen(int anzahl) {
        Set<Produkt> produkte = generiereSpeisen(RandomUtil.getRandom().nextInt(anzahl));
        List<Position> positionen =  produkte.stream().map(p -> new Position(p,RandomUtil.getRandom().nextInt(MAX_SPEISEN)+1)).collect(Collectors.toList());
        positionen.addAll(generiereGetraenke(RandomUtil.getRandom().nextInt(anzahl)).stream().map(p -> new Position(p,RandomUtil.getRandom().nextInt(MAX_GETRAENKE)+1)).collect(Collectors.toList()));
        return positionen;
    }

    private static Set<Produkt> generiereGetraenke(int anzahl) {
        HashSet<Produkt> produkte = new HashSet<>();
        int anzahlGetraenke = ProduktHelper.GETRAENKE.size();
        int maxAnzahl = Math.min(anzahl,anzahlGetraenke);
        while(produkte.size() < maxAnzahl) {
            produkte.add(ProduktHelper.GETRAENKE.get(RandomUtil.getRandom().nextInt(anzahlGetraenke)));
        }
        return produkte;
    }

    private static Set<Produkt> generiereSpeisen(int anzahl) {
        HashSet<Produkt> produkte = new HashSet<>();
        int anzahlProdukte = ProduktHelper.SPEISEN.size();
        int maxAnzahl = Math.min(anzahl,anzahlProdukte);
        while(produkte.size() < maxAnzahl) {
            produkte.add(ProduktHelper.SPEISEN.get(RandomUtil.getRandom().nextInt(anzahlProdukte)));
        }
        return produkte;
    }
}
