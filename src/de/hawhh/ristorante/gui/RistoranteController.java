package de.hawhh.ristorante.gui;

import de.hawhh.ristorante.model.*;
import de.hawhh.ristorante.modelgenerator.*;
import de.hawhh.ristorante.uimodelhelper.TreeModelHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    //wurde im SceneBuilder nicht gefunden.
    @FXML
    private ButtonBar positionButtonBar;

    @FXML
    private Button addButton;

    @FXML
    private Button positionenScanButton;

    @FXML
    private Button rechnungSaveButton;

    @FXML
    private ComboBox <Integer> jahrComboBox;

    @FXML
    private ComboBox <Month> monatComboBox;

    @FXML
    private ComboBox <ProduktKategorie> kategorieComboBox;

    @FXML
    private VBox scatterBox;

    @FXML
    private HBox saombo;






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

            nummerTextFeld.setEditable(false);
            datumTextFeld.setEditable(false);
            datumTextFeld.setTextFormatter(new TextFormatter<>(new LocalDateTimeStringConverter()));
            tischComboBox.setDisable(true);
            tischComboBox.setItems(FXCollections.observableArrayList(Tisch.values()));

            positionenScanButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("SCAN PRESSED");
                    positionenListView.setItems(FXCollections.observableArrayList(new PositionGenerator().generierePositionen(MAX_POSITIONEN)));
                    rechnungSaveButton.setDisable(false);
                }
            });

            rechnungSaveButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("SAVE PRESSED");
                    // String zu LOcalDatetime Konvertieren.
                    String date = datumTextFeld.getText();
                    LocalDateTime localdatetime = new LocalDateTimeStringConverter().fromString(date);
                    //LocalDateTime localdatetime = LocalDateTime.parse(date);
                    //neues RechnungObject erstellen
                    Rechnung neueRechnung;
                    if (tischComboBox.getValue() != null) {
                        neueRechnung = new Rechnung(nummerTextFeld.getText(), localdatetime, (Tisch) tischComboBox.getValue());
                        //  Positionen lesen und eintragen
                        List<Position> posList = positionenListView.getItems();
                        for (Position pos : posList) {
                            neueRechnung.add(pos);
                        }
                        //neues Rechnungsobjekt zum treeView hinzufügen.
                        // die neue Rechnung sichtbar machen und selektieren.
                        treeView.add(neueRechnung);

                        // aktualisieren Scatter.
                        scatterView.update(neueRechnung);
                        prepareBrowse();

                    }


                }
            });
            // wechseln in den Rechnungseditor
            addButton.setOnAction((ActionEvent e) ->{
                prepareEdit();
            });
        }


        void prepareBrowse() {
            positionButtonBar.getButtons().setAll(addButton);
            //wieder in die Ursprüngliche Detaileinsicht gewechselt

            //TreeView wird enabled.
            rechnungTreeView.setDisable(false);
        }

        private void prepareEdit() {
            positionButtonBar.getButtons().setAll(positionenScanButton,rechnungSaveButton);
            clearAll();

            nummerTextFeld.setText(RechnungGenerator.getRechnungNr());
            datumTextFeld.setText(new LocalDateTimeStringConverter().toString(LocalDateTime.now()));

            tischComboBox.setDisable(false);
            rechnungTreeView.setDisable(true);
        }


        void update(Rechnung rechnung) {
            nummerTextFeld.setText(rechnung.getNr());
            datumTextFeld.setText(new LocalDateTimeStringConverter().toString(rechnung.getDateTime()));
            positionenListView.setItems(FXCollections.observableArrayList(rechnung.getPositionen()));

            nummerTextFeld.setEditable(false);
            datumTextFeld.setEditable(false);
            positionenListView.setEditable(false);
            tischComboBox.setDisable(true);

            positionenListView.setItems(FXCollections.observableArrayList(rechnung.getPositionen()));
            this.rechnung = rechnung;

            nummerTextFeld.setText(rechnung.getNr());
            tischComboBox.getSelectionModel().select(rechnung.getTisch().ordinal());

            datumTextFeld.setText(rechnung.getDateTimeFormatted());

        }

        private void clearAll() {
            nummerTextFeld.clear();
            datumTextFeld.clear();
            tischComboBox.getSelectionModel().clearSelection();
            positionenListView.getItems().clear();
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


            // EventListener für den TreeView
            rechnungTreeView.getSelectionModel().selectedItemProperty().addListener(
                    ((observable, oldValue, newValue) -> {
                        if (newValue != null && newValue.getValue() instanceof Rechnung) {
                            //Aufruf der update(Rechnung) methode der DetaiView
                            detailView.update((Rechnung) newValue.getValue());
                        }
                    })
            );
        }

        public void add(Rechnung neueRechnung) {
            //rechnungen.add(neueRechnung);

            TreeItem<Object> tro = TreeModelHelper.append(rechnungTreeView.getRoot(), neueRechnung);
            rechnungTreeView.getSelectionModel().select(tro);
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
            monatComboBox.setDisable(true);

            jahrComboBox.setItems(FXCollections.observableArrayList(RechnungHelper.jahre(rechnungen)));
            kategorieComboBox.getItems().addAll(FXCollections.observableArrayList(Arrays.asList(ProduktKategorie.values())));

            jahrComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, month, newMonth) -> {
                monatComboBox.setDisable(false);
                monatComboBox.getItems().clear();
                monatComboBox.getItems().addAll(FXCollections.observableList(DateTimeUtil.legalMonthsPerYear(jahrComboBox.getValue())));
            });

            monatComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, month, newMonth) -> scatterVersuch());
            kategorieComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, month, newMonth) -> scatterVersuch());
        }

        private void scatterVersuch() {
            if (jahrComboBox.getValue() != null && monatComboBox.getValue() != null && kategorieComboBox.getValue() != null) {
                calculateScatter(jahrComboBox.getValue(), monatComboBox.getValue().getValue(), kategorieComboBox.getValue().getCorrespondingClass());
            }

        }

        private int minProduktAnzahl(int year, int month, Class<? extends Produkt> clazz) {
            return RechnungHelper.minProduktHaeufigkeitProMonat(rechnungen, year, month, clazz);
        }

        private int maxProduktAnzahl(int year, int month, Class<? extends Produkt> clazz) {
            return RechnungHelper.maxProduktHaeufigkeitProMonat(rechnungen, year, month, clazz);
        }

        public void update(Rechnung rechnung) {
            if (kategorieComboBox.getValue() != null && jahrComboBox.getValue() != null && monatComboBox.getValue() != null){
                List<Produkt> ausgewählteProdukte = ProduktHelper.get(kategorieComboBox.getValue().getCorrespondingClass());
                boolean schnittMenge = rechnung.getPositionen().stream().map(Position::getProduk).anyMatch(prod -> ausgewählteProdukte.contains(prod));
                if (schnittMenge && rechnung.getDate().getMonth() == monatComboBox.getValue() && rechnung.getDate().getYear() == jahrComboBox.getValue()){
                    rechnungen.add(rechnung);
                    //update(rechnung);
                    calculateScatter(jahrComboBox.getValue(), monatComboBox.getValue().getValue(), kategorieComboBox.getValue().getCorrespondingClass());
                }
            }
        }

        private void calculateScatter(int year, int month, Class<? extends Produkt> correspondingClass) {


            int xMax =  maxProduktAnzahl(year,month,correspondingClass);
            int xMin = minProduktAnzahl(year,month,correspondingClass);
            int tickUnitX = (xMax-xMin)/10;

            // Werte für die xAchse und yAchse bestimmen
            final Axis xAchse = new NumberAxis(xMin,xMax,tickUnitX);
            final Axis yAchse = new CategoryAxis(FXCollections.observableArrayList(DateTimeUtil.getDaysOfMonthAsString(year,month)));

            // Label für Achsen setzen
            yAchse.setLabel("Datum");
            xAchse.setLabel("Anzahl Produkt");

            ScatterChart rechnungScatter = new ScatterChart<>(xAchse,yAchse);

            List<Produkt> aktuelleProdukte = ProduktHelper.get(kategorieComboBox.getValue().getCorrespondingClass());



            // Serien erstellen.
            Map<Produkt, XYChart.Series> produktSeriesMap = aktuelleProdukte.stream().collect(Collectors.toMap(prod -> prod, prod -> {
                XYChart.Series series = new XYChart.Series();
                series.setName(prod.getName());
                return series;
            }));
            scatterBox.getChildren().add(rechnungScatter);


            //Rechnungen Sortiert nach Tagen
            Map<LocalDate,List<Rechnung>> rechnungTage = RechnungHelper.rechnungenNachTagen(rechnungen,DateTimeUtil.getDaysOfMonth(year, month));
            // Datum rausziehen
            for (Map.Entry<LocalDate,List<Rechnung>> entry : rechnungTage.entrySet() ) {
                String datum = DateTimeUtil.DATE_FORMATTER.format(entry.getKey());
                // Produkte mit Ihren Häufigkeiten für jedes Datum
                Map<Produkt,Integer> produktAnzahl = RechnungHelper.produktHaeufigkeiten(entry.getValue(), correspondingClass);
                for (Map.Entry<Produkt,Integer> gemapt : produktAnzahl.entrySet()) {
                    // Fügen wir die Anzahlen der Produkte mit dem jeweilige Datum dem YXChart hinzu
                    produktSeriesMap.get(gemapt.getKey()).getData().add(new XYChart.Data(gemapt.getValue(), datum));
                }
            }

            produktSeriesMap.values().forEach(series -> rechnungScatter.getData().add(series));

            scatterBox.getChildren().clear();
            scatterBox.getChildren().addAll(saombo);
            scatterBox.getChildren().add(rechnungScatter);

            AnchorPane.setTopAnchor(rechnungScatter,1.0);
            AnchorPane.setBottomAnchor(rechnungScatter,1.0);
            AnchorPane.setLeftAnchor(rechnungScatter,1.0);
            AnchorPane.setRightAnchor(rechnungScatter,1.0);

        }
    }
}