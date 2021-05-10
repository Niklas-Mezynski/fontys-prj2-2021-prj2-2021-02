package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Airport;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.g02.flightsalesfx.App.setRoot;

public class CreateRouteController implements Controller {

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
        final var allAirports = App.businessLogicAPI.getAllAirports(Airport -> true);
//        allAirports.sort((a, b) -> a.getName().compareTo(b.getName()));

        listDep.getItems().addAll(allAirports);
        listArr.getItems().addAll(allAirports);

        searchDep.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            listDep.getItems().clear();
            listDep.getItems().addAll(allAirports.stream().filter(a->a.getName().toLowerCase().contains(newValue.toLowerCase())).collect(Collectors.toList()));
        }));

        searchArr.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            listArr.getItems().clear();
            listArr.getItems().addAll(allAirports.stream().filter(a->a.getName().toLowerCase().contains(newValue.toLowerCase())).collect(Collectors.toList()));
        }));
    }


    @FXML
    void addRoute(ActionEvent event) throws IOException {
        Airport depAirport = listDep.getSelectionModel().getSelectedItem();
        Airport arrAirport = listArr.getSelectionModel().getSelectedItem();

        boolean routeCreated = false;
        if( (depAirport != null && arrAirport != null) && !(depAirport.equals(arrAirport))) {
            List<Route> routes = App.businessLogicAPI.getAllRoutes(route -> {
                if(route.getDepartureAirport().equals(depAirport) && route.getArrivalAirport().equals(arrAirport)){
                    return true;
                } else {
                    return false;
                }
            });
            if(routes.size() != 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("This Route already Exists");
                alert.setContentText("You selected a combination of Departure and Arrival, that already exists.");
                alert.showAndWait();
            } else {
                routeCreated = App.businessLogicAPI.createRouteFromUI(depAirport, arrAirport);
                if (routeCreated) {
                    exit();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("There was an error Saving the route");
                    alert.setContentText("There was an error while saving the created route. Try again!");
                    alert.showAndWait();
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("You have to select two different airports");
            alert.setContentText("You either have selected the same airport twice or not selected two airports");
            alert.showAndWait();
        }

    }

    /**
     * Exit the current scene and return to the previous one, using the static setRoot method of App
     * @see App#setRoot(String)
     */
    @FXML
    private void exit() throws IOException {
        if(App.comesFromCreateFlight){
            App.comesFromCreateFlight = false;
            setRoot("createFlight");
        }else{
            setRoot("home");
        }
    }
}
