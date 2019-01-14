package de.hawhh.ristorante.gui;

import de.hawhh.ristorante.model.Produkt;
import de.hawhh.ristorante.model.Rechnung;
import de.hawhh.ristorante.modelgenerator.RechnungGenerator;
import de.hawhh.ristorante.modelgenerator.RechnungHelper;
import de.hawhh.ristorante.uimodelhelper.TreeModelHelper;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class RistoranteController {
    private static final int MAX_RECHNUNGEN = 2000;
    private static final int MAX_POSITIONEN = 20;
    private static final int MIN_POSITIONEN = 4;

    private RechnungDetailView detailView;
    private RechnungTreeView treeView;
    private RechnungScatterView scatterView;

    @FXML
    private TreeView<Object> rechnungTreeView;

    @FXML
    private TextField datumTextFeld;

    @FXML
    private TextField nummerTextFeld;

    @FXML
    private ComboBox tischComboBox;

    @FXML
    private ListView positionenListView;

    @FXML
    private Button addButton;



    @FXML
    protected void initialize() {

        List<Rechnung> rechnungen = RechnungGenerator.generiereRechnungen(MAX_RECHNUNGEN);
        treeView = new RechnungTreeView(rechnungen);
        treeView.initialize();
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

         /*
         Erzeugen der TreeView auf Basis einer generierten Rechnungsliste
         */
         TreeItem<Object> treeItemRoot = (TreeItem<Object>) TreeModelHelper.generiereRechnungsBaumNachJahrMonatTag(new TreeItem<>("Rechnungen nach Datum"), RechnungGenerator.generiereRechnungen(MAX_RECHNUNGEN));

         /*
         * Verknüpfen
         * */
         rechnungTreeView.setRoot(treeItemRoot);
         rechnungTreeView.setShowRoot(true);
         rechnungTreeView.getRoot().setExpanded(true);

         // initialize für die Controls mit statischem Inhalt.

        }


        void prepareBrowse() {

        }

        void prepareEdit() {

            //TreeItem<Rechnung> treeItem = (TreeItem<Rechnung>) rechnungTree.getSelectionModel().getSelectedItems();
            //treeItem.getValue().getNr();


            nummerTextFeld.setText(RechnungGenerator.getRechnungNr());

            // formatieren des LocalDateTime-Elements in einen String für Datumfeld
            //LocalDateTime currentDateTime = LocalDateTime.now();
            //DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            //String formattedDateTime = currentDateTime.format(formatter);

            // datumFeld.setText(formattedDateTime);

            datumTextFeld.setTextFormatter( new TextFormatter<>(new LocalDateTimeStringConverter()));

            datumTextFeld.setText(String.valueOf(LocalDateTime.now()));
            tischComboBox.setDisable(true);
            positionenListView.getSelectionModel().clearSelection();

        }

        void update(Rechnung rechnung) {
            this.rechnung = rechnung;
            nummerTextFeld.setText(rechnung.getNr());
            tischComboBox.getSelectionModel().select(rechnung.getTisch().ordinal());
            datumTextFeld.setText(rechnung.getDateTime().toString());
            // datumFeld.setTextFormatter(new TextFormatter<>(new LocalDateTimeStringConverter()));
            // positionFeld
            for (int i = 0; i < rechnung.getPositionen().size(); i++) {
                // TODO positionListView.setItems(rechnung.getPositionen().get(i));
            }
        }

        private void clearAll() {
            nummerTextFeld.clear();
            datumTextFeld.clear();
            tischComboBox.getSelectionModel().select(null);
            positionenListView.getSelectionModel().select(null);
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
            rechnungen.add(neueRechnung);
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

        private int minProduktAnzahl(int year, int month, Class<? extends Produkt> clazz) {
            return RechnungHelper.minProduktHaeufigkeitProMonat(rechnungen, year, month, clazz);
        }

        private int maxProduktAnzahl(int year, int month, Class<? extends Produkt> clazz) {
            return RechnungHelper.maxProduktHaeufigkeitProMonat(rechnungen, year, month, clazz);
        }

        public void update(Rechnung rechnung) {
        }

        private void calculateScatter(int year, int month, Class<? extends Produkt> correspondingClass) {

        }
    }
}