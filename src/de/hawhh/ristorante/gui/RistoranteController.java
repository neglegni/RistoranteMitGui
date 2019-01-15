package de.hawhh.ristorante.gui;

import de.hawhh.ristorante.model.Produkt;
import de.hawhh.ristorante.model.ProduktKategorie;
import de.hawhh.ristorante.model.Rechnung;
import de.hawhh.ristorante.model.Tisch;
import de.hawhh.ristorante.modelgenerator.*;
import de.hawhh.ristorante.uimodelhelper.TreeModelHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateTimeStringConverter;

import javax.swing.text.html.parser.Parser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static de.hawhh.ristorante.uimodelhelper.TreeModelHelper.append;
import static javafx.scene.input.KeyCode.Y;


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
    private ComboBox jahrComboBox;

    @FXML
    private ComboBox monatComboBox;

    @FXML
    private ComboBox kategorieComboBox;






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
        //private Button positionenScanButton = new Button("Scan");
        //private Button rechnungSaveButton = new Button("Speichern");

        RechnungDetailView() {
            initialize();
        }

        private void initialize() {

            tischComboBox.setItems(FXCollections.observableArrayList(Tisch.values()));
            nummerTextFeld.setEditable(true);
            datumTextFeld.setEditable(true);
            tischComboBox.setDisable(true);
            rechnungSaveButton.setVisible(false);
            positionenScanButton.setVisible(false);
            //positionenListView.setItems(null);


            // Funktionen für den Plus-Button.
            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    prepareEdit();
                }
            });

            positionenScanButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    positionenListView.setItems(FXCollections.observableArrayList(new PositionGenerator().generierePositionen(MAX_POSITIONEN)));
                    rechnungSaveButton.setDisable(false);
                }
            });

            rechnungSaveButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // String zu LOcalDatetime Konvertieren.
                    String date = datumTextFeld.getText();
                    LocalDateTime localdatetime = LocalDateTime.parse(date);
                    //neues RechnungObject erstellen
                    if (tischComboBox.getValue() != null) {
                        Rechnung neueRechnung = new Rechnung(nummerTextFeld.getText(), localdatetime, (Tisch) tischComboBox.getValue());
                        //neues Rechnungsobjekt zum treeView hinzufügen.
                        // die neue Rechnung sichtbar machen und selektieren.
                        treeView.add(neueRechnung);
                    }

                    // TODO neues Element im TreeView aufklappen.
                    //rechnungTreeView.refresh();
                    //rechnungTreeView.getSeSlectionModel().select(rechnungTreeView.getSelectionModel().getSelectedIndex());

                    //TODO if es existiert ein Scatterplott, dann hier aktualisieren.

                    //wieder in die Ursprüngliche Detaileinsicht gewechselt

                    //TreeView wird enabled.
                    rechnungTreeView.setDisable(false);
                }
            });

        }

        void prepareBrowse() {

        }

        void prepareEdit() {
            addButton.setVisible(false);
            rechnungSaveButton.setVisible(true);
            rechnungSaveButton.setDisable(true);
            positionenScanButton.setVisible(true);

            nummerTextFeld.setText(RechnungGenerator.getRechnungNr());
            datumTextFeld.setText(String.valueOf(LocalDateTime.now()));
            tischComboBox.setDisable(false);
            positionenListView.getSelectionModel().clearSelection();
            rechnungTreeView.setDisable(true);
        }

        void update(Rechnung rechnung) {
            nummerTextFeld.setText(rechnung.getNr());
            //datumTextFeld.setTextFormatter(new TextFormatter<>(new LocalDateTimeStringConverter()));
            datumTextFeld.setText(new LocalDateTimeStringConverter().toString(rechnung.getDateTime()));
            tischComboBox.setItems(FXCollections.observableArrayList(rechnung.getTisch()));
            positionenListView.setItems(FXCollections.observableArrayList(rechnung.getPositionen()));

            nummerTextFeld.setEditable(false);
            datumTextFeld.setTextFormatter(new TextFormatter<>(new LocalDateTimeStringConverter()));
            datumTextFeld.setEditable(false);
            positionenListView.setEditable(false);
            tischComboBox.setDisable(true);

            positionenListView.setItems(FXCollections.observableArrayList(rechnung.getPositionen()));

            // wechseln in den Rechnungseditor
            addButton.setOnAction((ActionEvent e) ->{
                //clearing der Controls für eine neue Rechnung.
                nummerTextFeld.clear();
                datumTextFeld.clear();
                tischComboBox.getSelectionModel().select(null);
                positionenListView.getItems().clear();
                addButton.setVisible(false);
                positionenScanButton.setVisible(true);
                rechnungSaveButton.setDisable(true);
                rechnungSaveButton.setVisible(true);
                if(rechnung.getPositionen() != null) {
                    rechnungSaveButton.setDisable(false);
                }

            });


            this.rechnung = rechnung;
            nummerTextFeld.setText(rechnung.getNr());
            tischComboBox.getSelectionModel().select(rechnung.getTisch().ordinal());
            datumTextFeld.setText(rechnung.getDateTime().toString());

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


        void setDisable(boolean disabled) {

        }

        public void add(Rechnung neueRechnung) {
            append(rechnungTreeView.getRoot(), neueRechnung);
            //TODO
            //rechnungTreeView.getSelectionModel().select(TreeItem<>(neueRechnung));
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
            kategorieComboBox.setItems(FXCollections.observableArrayList(ProduktKategorie.values()));

            jahrComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if(newValue != null) {
                        monatComboBox.setDisable(false);
                        monatComboBox.setItems(FXCollections.observableArrayList(DateTimeUtil.legalMonthsPerYear((Integer)jahrComboBox.getValue())));

                    }
                }
            });

            monatComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (monatComboBox != null && jahrComboBox != null && kategorieComboBox != null) {
                        scatterView.initialize();
                    }
                }
            });

            kategorieComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (monatComboBox != null && jahrComboBox != null && kategorieComboBox != null) {
                        scatterView.initialize();
                    }
                }
            });
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
            List products = ProduktHelper.get(correspondingClass);
            int max = RechnungHelper.maxProduktHaeufigkeitProMonat(products, year,month,correspondingClass);
            int min = RechnungHelper.minProduktHaeufigkeitProMonat(products,year,month,correspondingClass);

            new CategoryAxis().setCategories(FXCollections.observableArrayList(DateTimeUtil.getDaysOfMonthAsString(year, month)));
            for (Object prod: products) {
                XYChart.Series<Integer,Integer> series = new XYChart.Series<>();
                series.setName(correspondingClass.getName());
            }
            //TODO stehen geblieben Seite 11/13 Punkt "3.".
        }
    }
}