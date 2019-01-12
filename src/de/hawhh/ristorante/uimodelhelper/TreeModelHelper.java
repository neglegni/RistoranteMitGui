package de.hawhh.ristorante.uimodelhelper;

import de.hawhh.ristorante.model.Rechnung;
import javafx.scene.control.TreeItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeModelHelper {

    public static TreeItem<Object> generiereRechnungsBaumNachJahrMonatTag(TreeItem<Object> root, List<Rechnung> rechnungen) {

      List<TreeItem<Object>> nachJahr =  rechnungen.stream().
                collect(Collectors.groupingBy(r1 -> r1.getDate().getYear())).entrySet().stream().map(entry -> {

          TreeItem<Object> nachJahrItem = new TreeItem<>(entry.getKey());
          nachJahrItem.getChildren().addAll(generiereRechnungsBaumNachMonat(entry.getValue()));
          return nachJahrItem;
      }).collect(Collectors.toList());

      root.getChildren().addAll(nachJahr);
      return root;
    }

    private static List<TreeItem<Object>> generiereRechnungsBaumNachMonat(List<Rechnung> rechnungen){
        return rechnungen.stream().collect(Collectors.groupingBy(r -> DateTimeFormatter.ofPattern("MM").format(r.getDate()))).
                entrySet().stream().map(entry-> {
                    TreeItem<Object> nachMonatItem = new TreeItem<>(entry.getKey());
                    nachMonatItem.getChildren().addAll(generiereRechnungsBaumNachTag(
                            entry.getValue().stream().sorted((r1,r2) -> r1.getDate().getMonth().compareTo(r2.getDate().getMonth())).collect(Collectors.toList())));
                    return nachMonatItem;
        }).sorted(Comparator.comparing(item -> (String)item.getValue())).collect(Collectors.toList());
    }

    private static List<TreeItem<Object>> generiereRechnungsBaumNachTag(List<Rechnung> rechnungen){
        return rechnungen.stream().collect(Collectors.groupingBy(r -> DateTimeFormatter.ofPattern("dd.MM.yy").format(r.getDate()))).
                entrySet().stream().map(entry-> {
            TreeItem<Object> nachTagItem = new TreeItem<>(entry.getKey());
            nachTagItem.getChildren().addAll(entry.getValue().stream().sorted(Comparator.comparing(Rechnung::getDateTime)).
                    map((Rechnung r) -> new TreeItem<Object>(r)).collect(Collectors.toList()));
            return nachTagItem;
        }).sorted(Comparator.comparing(item -> (String)item.getValue())).collect(Collectors.toList());
    }

    public static TreeItem<Object> append(TreeItem<Object> root, Rechnung rechnung) {
        TreeItem<Object> lastYearItem = root.getChildren().get(root.getChildren().size()-1);
        TreeItem<Object> lastMonth = lastYearItem.getChildren().get(lastYearItem.getChildren().size()-1);
        TreeItem<Object> lastDay = lastMonth.getChildren().get(lastMonth.getChildren().size()-1);
        TreeItem<Object> addedItem = new TreeItem<>(rechnung);
        lastDay.getChildren().add(addedItem);
        return addedItem;

    }

}
