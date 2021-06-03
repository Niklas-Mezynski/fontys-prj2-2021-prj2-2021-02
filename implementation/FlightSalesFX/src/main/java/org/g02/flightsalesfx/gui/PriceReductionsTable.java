package org.g02.flightsalesfx.gui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.g02.flightsalesfx.businessEntities.PriceReduction;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class PriceReductionsTable extends TableView<PriceReduction> {
    public PriceReductionsTable(List<PriceReduction> prices, RowClickHandler<PriceReduction> clickHandler) {
        TableColumn<PriceReduction,String > nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(v->{return new SimpleStringProperty(v.getValue().getName());});
        TableColumn<PriceReduction,String > redColumn = new TableColumn<>("Reduction");
        redColumn.setCellValueFactory(v->{
            var perc= new SimpleStringProperty(v.getValue().getPercentageAsDouble()+"");
            var isPerc=new SimpleBooleanProperty(v.getValue().isPercentage());
            if(isPerc.get()){
                perc.setValue(perc.getValue()+"%");
            }else{
                perc.setValue(perc.getValue()+"€");
            }
            return perc;
        });
        TableColumn<PriceReduction,String > startColumn = new TableColumn<>("Start Date");
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        startColumn.setCellValueFactory(v->{return new SimpleStringProperty(v.getValue().getStartTime().format(myFormatObj).substring(0,10));});
        TableColumn<PriceReduction,String > endColumn = new TableColumn<>("End Date");
        endColumn.setCellValueFactory(v->{return new SimpleStringProperty(v.getValue().getEndTime().format(myFormatObj).substring(0,10));});
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getItems().addAll(prices);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(nameColumn, redColumn, startColumn, endColumn);
        setRowFactory(planeTableView -> {
            TableRow<PriceReduction> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row;
        });
    }
}
