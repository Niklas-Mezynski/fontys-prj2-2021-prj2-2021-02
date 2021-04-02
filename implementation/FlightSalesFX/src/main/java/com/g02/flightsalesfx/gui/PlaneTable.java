package com.g02.flightsalesfx.gui;

import com.g02.flightsalesfx.businessEntities.Plane;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class PlaneTable extends TableView<Plane> {

    public PlaneTable(List<Plane> planes, RowClickHandler<Plane> clickHandler) {
        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn idColumn = new TableColumn("Type");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn manufColumn = new TableColumn("Manufacturer");
        manufColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        TableColumn seatColumn = new TableColumn("Seats");
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seatCount"));
        TableColumn rowColumn = new TableColumn("Rows");
        rowColumn.setCellValueFactory(new PropertyValueFactory<>("rowCount"));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getItems().addAll(planes);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(idColumn, nameColumn, manufColumn, seatColumn, rowColumn);
        setRowFactory(planeTableView -> {
            TableRow<Plane> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row ;
        });
    }
}
