package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Airport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;

import java.io.IOException;

import static com.g02.flightsalesfx.App.setRoot;

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

    @FXML
    private Button buttonExit;

    public void initialize() {
        var allAirports = App.businessLogicAPI.getAllAirports(Airport -> true);
//        allAirports.sort((a, b) -> a.getName().compareTo(b.getName()));

        listDep.getItems().addAll(allAirports);
        listArr.getItems().addAll(allAirports);

        searchDep.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            var searchedAirports = App.businessLogicAPI.getAllAirports(Airport ->
                    Airport.getName().toLowerCase().contains(newValue.toLowerCase()));
            listDep.getItems().clear();
            listDep.getItems().addAll(searchedAirports);
        }));

        searchArr.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            var searchedAirports = App.businessLogicAPI.getAllAirports(Airport ->
                    Airport.getName().toLowerCase().contains(newValue.toLowerCase()));
            listArr.getItems().clear();
            listArr.getItems().addAll(searchedAirports);
        }));
    }


    @FXML
    void addRoute(ActionEvent event) throws IOException {
        Airport depAirport = listDep.getSelectionModel().getSelectedItem();
        Airport arrAirport = listArr.getSelectionModel().getSelectedItem();

        boolean routeCreated = false;
        if( (depAirport != null && arrAirport != null) && !(depAirport.equals(arrAirport))) {
            routeCreated = App.businessLogicAPI.createRouteFromUI(depAirport, arrAirport);
        }

        if (routeCreated) {
            exit();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving route");
            alert.setContentText("There was an error while saving the created route. Try again!");
            alert.showAndWait();
        }
    }

    /**
     * Exit the current scene and return to the previous one, using the static setRoot method of App
     * @see App#setRoot(String)
     */
    @FXML
    private void exit() throws IOException {
        setRoot("home");
    }
}
