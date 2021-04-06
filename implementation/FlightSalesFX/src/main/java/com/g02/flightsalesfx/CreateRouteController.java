package com.g02.flightsalesfx;

<<<<<<< Updated upstream
public class CreateRouteController {
    
=======
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
    private TextField depTextField;

    @FXML
    private TextField arrTextField;


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
        System.out.println();
    }

>>>>>>> Stashed changes
}
