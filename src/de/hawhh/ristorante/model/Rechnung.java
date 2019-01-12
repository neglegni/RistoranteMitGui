package de.hawhh.ristorante.model;

import de.hawhh.ristorante.modelgenerator.PositionGenerator;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rechnung implements Iterable<Position> {

    private final String nr;
    private final List<Position> positionen;
    private final LocalDateTime dateTime;
    private final Tisch tisch;

    public Rechnung(String nr, LocalDateTime dateTime, Tisch tisch){
        this.nr = nr;
        this.dateTime = dateTime;
        this.tisch = tisch;
        this.positionen = new ArrayList<Position>();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getDate(){
        return getDateTime().toLocalDate();
    }

    public Tisch getTisch() {
        return tisch;
    }

    public String getNr() {
        return nr;
    }

    public List<Position> getPositionen() {
        return new ArrayList<>(positionen);
    }

    public void add(Position pos) {
        this.positionen.add(pos);
    }

    public void addAll(List<Position> positionen) {this.positionen.addAll(positionen);}

    public int betrag(){
        return positionen.stream().map(Position::getAnzahl).reduce(0,(acc, preis) -> acc+preis);
    }


    public String getDateTimeFormatted(){
        return new LocalDateTimeStringConverter().toString(getDateTime());
    }
    @Override
    public String toString() {
        return  nr +"(" +betrag()+ "€)" +
                " vom " +getDateTimeFormatted() +
                " " + tisch +
                ", positionen=" + positionen;
    }


    @Override
    public Iterator<Position> iterator() {
        return positionen.iterator();
    }
}
