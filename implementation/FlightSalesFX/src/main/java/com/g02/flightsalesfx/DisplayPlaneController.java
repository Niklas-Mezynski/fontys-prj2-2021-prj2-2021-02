package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Seat;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DisplayPlaneController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        TableView<Plane> table=new TableView<>();
        List<Plane> data= App.persistenceAPI.getPlaneStorageService(App.businessLogicAPI.getPlaneManager()).getAll();
        TableColumn nameColumn=new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn idColumn=new TableColumn("Type");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn manufColumn=new TableColumn("Manufacturer");
        manufColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        TableColumn seatColumn=new TableColumn("Seats");
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seatCount"));
        TableColumn rowColumn=new TableColumn("Rows");
        rowColumn.setCellValueFactory(new PropertyValueFactory<>("rowCount"));
        table.getItems().addAll(data);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getColumns().addAll(idColumn,nameColumn,manufColumn,seatColumn,rowColumn);
        VBox vbox=new VBox();
        vbox.getChildren().add(table);
        Scene scene=new Scene(vbox);
        stage.setScene(scene);
        stage.show();
        }
}
