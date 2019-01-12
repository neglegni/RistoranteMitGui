package de.hawhh.ristorante.modelgenerator;

import de.hawhh.ristorante.model.Position;
import de.hawhh.ristorante.model.Produkt;
import de.hawhh.ristorante.model.Rechnung;

import java.awt.color.CMMException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class RechnungHelper {

    public static Map<Produkt,Integer> produktHaeufigkeiten(List<Rechnung> rechnungen){
        return rechnungen.stream().map(r -> r.getPositionen()).flatMap(List::stream).
                collect(Collectors.toMap(pos -> pos.getProduk(), pos -> pos.getAnzahl(), (acc,pos) -> acc+pos )
        );
    }

    public static List<Integer> jahre(List<Rechnung> rechnungen){
        return rechnungen.stream().map(r-> r.getDate().getYear()).distinct().sorted().collect(Collectors.toList());
    }

    public static Map<Produkt,Integer> produktHaeufigkeiten(List<Rechnung> rechnungen,Class<? extends Produkt> clazz){
        return rechnungen.stream().map(r -> r.getPositionen()).flatMap(List::stream).filter(pos-> pos.getProduk().getClass()==clazz).
                collect(Collectors.toMap(pos -> pos.getProduk(), pos -> pos.getAnzahl(), (acc,pos) -> acc+pos )
                );
    }

    public static int maxProduktHaeufigkeitProMonat(List<Rechnung> rechnungen, int year,int month,Class<? extends Produkt> clazz){
        Map<LocalDate,List<Rechnung>>  rechnungenNachTagen = rechnungenNachTagen(rechnungen,DateTimeUtil.getDaysOfMonth(year,month));
        Optional<Map.Entry<LocalDate,Integer>> maxEntry = rechnungenNachTagen.entrySet().stream().
                collect(Collectors.toMap(entry -> entry.getKey(),
                        entry -> {
                            List<Rechnung> rechnungenProTag = entry.getValue();
                            Map<Produkt,Integer> haeufigkeitenProTag = produktHaeufigkeiten(rechnungenProTag,clazz);
                            Optional<Map.Entry<Produkt,Integer>> maxEntryPerDay = haeufigkeitenProTag.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue));
                            return maxEntryPerDay.isPresent() ? maxEntryPerDay.get().getValue() : Integer.MAX_VALUE;
                        })).entrySet().stream().max(Comparator.comparing(Map.Entry::getValue));

        return maxEntry.isPresent() ? maxEntry.get().getValue() : 0;
    }
    public static int minProduktHaeufigkeitProMonat(List<Rechnung> rechnungen, int year,int month, Class<? extends Produkt> clazz){
        Map<LocalDate,List<Rechnung>>  rechnungenNachTagen = rechnungenNachTagen(rechnungen,DateTimeUtil.getDaysOfMonth(year,month));
        Optional<Map.Entry<LocalDate,Integer>> minEntry = rechnungenNachTagen.entrySet().stream().
                collect(Collectors.toMap(entry -> entry.getKey(),
                        entry -> {
                            List<Rechnung> rechnungenProTag = entry.getValue();
                            Map<Produkt,Integer> haeufigkeitenProTag = produktHaeufigkeiten(rechnungenProTag,clazz);
                            Optional<Map.Entry<Produkt,Integer>> minEntryPerDay = haeufigkeitenProTag.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue));
                            return minEntryPerDay.isPresent() ? minEntryPerDay.get().getValue() : Integer.MAX_VALUE;
                        })).entrySet().stream().min(Comparator.comparing(Map.Entry::getValue));

        return minEntry.isPresent() ? minEntry.get().getValue() : 0;
    }

    public static Map<LocalDate,List<Rechnung>> rechnungenNachTagen(List<Rechnung> rechnungen, List<LocalDate> tage){
        return rechnungen.stream().filter(r -> tage.contains(r.getDate())).collect(Collectors.groupingBy(Rechnung::getDate));
    }
}
