package de.hawhh.ristorante.gui;

import de.hawhh.ristorante.model.*;
import de.hawhh.ristorante.modelgenerator.*;
import de.hawhh.ristorante.uimodelhelper.TreeModelHelper;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


public class RistoranteController {
    private static final int MAX_RECHNUNGEN = 2000;
    private static final int MAX_POSITIONEN = 20;
    private static final int MIN_POSITIONEN = 4;

    private RechnungDetailView detailView;
    private RechnungTreeView treeView;
    private RechnungScatterView scatterView;


    @FXML
    protected void initialize() {

        List<Rechnung> rechnungen = RechnungGenerator.generiereRechnungen(MAX_RECHNUNGEN);
        treeView = new RechnungTreeView(rechnungen);
        detailView = new RechnungDetailView();
        scatterView = new RechnungScatterView(rechnungen);
    }


    class RechnungDetailView {
        private final String titel = "Rechnung";
        private Rechnung rechnung;
        private Button positionenScanButton = new Button("Scan");
        private Button rechnungSaveButton = new Button("Speichern");

        RechnungDetailView() {
            initialize();
        }

        private void initialize() {

        }

        void prepareBrowse() {

        }

        void prepareEdit() {
        }

        void update(Rechnung rechnung) {
        }

        private void clearAll() {
        }

    }

    class RechnungTreeView {
        private final String rootNodeName = "Rechnungen";
        private final List<Rechnung> rechnungen;

        RechnungTreeView(List<Rechnung> rechnungen) {
            this.rechnungen = rechnungen;
            initialize();
        }

        private void initialize() {
        }

        void setDisable(boolean disabled) {

        }

        public void add(Rechnung neueRechnung) {
        }
    }

    class RechnungScatterView {

        private final List<Rechnung> rechnungen;
        private final int xUnits = 40;
        private final int xOFFSET = 2;

        RechnungScatterView(List<Rechnung> rechnungen) {
            this.rechnungen = rechnungen;
            initialize();
        }

        private void initialize() {

        }

        private int minProduktAnzahl(int year, int month,Class<? extends Produkt> clazz) {
            return RechnungHelper.minProduktHaeufigkeitProMonat(rechnungen, year, month,clazz);
        }

        private int maxProduktAnzahl(int year, int month,Class<? extends Produkt> clazz) {
            return RechnungHelper.maxProduktHaeufigkeitProMonat(rechnungen, year, month,clazz);
        }

        public void update(Rechnung rechnung){
        }

        private void calculateScatter(int year, int month, Class<? extends Produkt> correspondingClass){

        }
    }
}