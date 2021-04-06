package com.g02.flightsalesfx.gui;

import com.g02.flightsalesfx.businessEntities.Route;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RouteTable extends TableView<Route> {

    public RouteTable (List<Route> routes) {

        TableColumn departureColumn = new TableColumn("Departure");
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departureAirport"));
        TableColumn arrivalColumn = new TableColumn("Arrival");
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalAirport"));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getItems().addAll(routes);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(departureColumn, arrivalColumn);
    }
}
