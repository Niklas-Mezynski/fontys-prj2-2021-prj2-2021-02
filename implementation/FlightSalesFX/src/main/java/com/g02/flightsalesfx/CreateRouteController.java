package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CreateRouteController {

    @FXML
    private ListView<Airport> listArr;

    @FXML
    private ListView<Airport> listDep;

    @FXML
    private TextField searchDep;

    @FXML
    private TextField searchArr;

    @FXML
    private Label lableDep;

    @FXML
    private Label labelArr;

    @FXML
    private Button buttonAdd;

    public void initialize() {
        var allAirports = App.businessLogicAPI.getAllAirports();
        allAirports.sort((a, b) -> a.getName().compareTo(b.getName()));

        listDep.getItems().addAll(allAirports);
        listArr.getItems().addAll(allAirports);
    }


    @FXML
    void addRoute(ActionEvent event) {
        Airport depAirport = listDep.getSelectionModel().getSelectedItem();
        Airport arrAirport = listArr.getSelectionModel().getSelectedItem();
        System.out.println("Departure:" + depAirport +"\n Arrival: " + arrAirport);
    }

    @FXML
    void updateArrList(ActionEvent event) {

    }

    @FXML
    void updateDepList(ActionEvent event) {

    }

}
