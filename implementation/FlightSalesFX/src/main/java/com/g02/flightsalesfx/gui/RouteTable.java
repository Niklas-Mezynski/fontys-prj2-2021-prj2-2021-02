package com.g02.flightsalesfx.gui;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Route;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RouteTable extends TableView<Route> {

    public RouteTable (List<Route> routes, RowClickHandler<Route> clickHandler) {

        TableColumn departureColumn = new TableColumn("Departure");
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departureAirport"));
        TableColumn arrivalColumn = new TableColumn("Arrival");
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalAirport"));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Route, String> enabledColumn = new TableColumn("Enabled");
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //Extract EnableStatus from Route field "rteEnable"
        enabledColumn.setCellValueFactory(param -> {
            Route rte = param.getValue();
            if(rte.getEnabled()){
                return new SimpleStringProperty("Enabled");
            }else{
                return new SimpleStringProperty("Disabled");
            }

        });





        getItems().addAll(routes);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(departureColumn, arrivalColumn, enabledColumn);
        setRowFactory(rteTableView -> {
            TableRow<Route> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row ;
        });
    }
}
