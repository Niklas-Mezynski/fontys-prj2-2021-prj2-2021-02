package org.g02.flightsalesfx.gui;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.g02.flightsalesfx.businessEntities.PriceReduction;

import java.util.List;

public class PriceReductionsTable extends TableView<PriceReduction> {
    public PriceReductionsTable(List<PriceReduction> prices, RowClickHandler<PriceReduction> clickHandler) {
        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn redColumn = new TableColumn("Reduction");
        redColumn.setCellValueFactory(new PropertyValueFactory<>("reduction"));
        TableColumn startColumn = new TableColumn("Start Date");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn endColumn = new TableColumn("End Date");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn percentColumn = new TableColumn("%");
        percentColumn.setCellValueFactory(new PropertyValueFactory<>("inPercent"));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getItems().addAll(prices);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(nameColumn, redColumn, startColumn, endColumn, percentColumn);
        setRowFactory(planeTableView -> {
            TableRow<PriceReduction> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row;
        });
    }
}
