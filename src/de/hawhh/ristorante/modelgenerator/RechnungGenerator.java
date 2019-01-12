package de.hawhh.ristorante.modelgenerator;

import de.hawhh.ristorante.model.Rechnung;
import de.hawhh.ristorante.model.Tisch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class RechnungGenerator {

    private static int rechnungNr = 1;
    private static int MAX_POSITIONEN = 50;
    private static List<Rechnung> rechnungen = null;

    public static String getRechnungNr(){
        return "R"+rechnungNr++;
    }

    public static List<Rechnung> generiereRechnungen(int anzahl){
        if (rechnungen == null) {
            rechnungen = new ArrayList<>();
            int anzahlTische = Tisch.values().length;
            LocalDateTime now = LocalDateTime.now();
            for (int i = 0; i < anzahl; i++) {
                Tisch tisch = Tisch.values()[RandomUtil.getRandom().nextInt(anzahlTische)];
                LocalDateTime dateTime = RandomUtil.randomInYears(1, now);
                Rechnung rechnung = new Rechnung(getRechnungNr(), dateTime, tisch);
                rechnung.addAll(PositionGenerator.generierePositionen(MAX_POSITIONEN));
                rechnungen.add(rechnung);
            }
        }
        return rechnungen;
    }

}
