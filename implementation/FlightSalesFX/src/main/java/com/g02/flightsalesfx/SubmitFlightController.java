package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessEntities.SalesOfficer;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPIImpl;
import com.g02.flightsalesfx.gui.PlaneTable;
import com.g02.flightsalesfx.gui.RouteTable;
import com.g02.flightsalesfx.persistence.EmployeeStorageServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

import static com.g02.flightsalesfx.App.setRoot;

public class SubmitFlightController {

    @FXML
    private TextField planeSearchBar;

    @FXML
    private AnchorPane planeTablePane;

    @FXML
    private TextField flightPrice;

    @FXML
    private Button saveFlightButton;

    @FXML
    private Button exitFlightButton;

    @FXML
    private TextField flightNumberTextField;

    private List<Plane> selectedPlanes;
    private PlaneTable planeTable;
    private Plane selectedPlane = null;

    public void initialize() {
        selectedPlanes = App.businessLogicAPI.getAllPlanes(route -> true);

        planeSearchBar.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            updatePlanes(newValue);
        }));

        createPlaneTableWithData(selectedPlanes);


    }

    private void createPlaneTableWithData(List<Plane> planes){
        planeTablePane.getChildren().remove(planeTable);
        planeTable = new PlaneTable(planes, (event, row) -> {
            if (!row.isEmpty()) {
                Plane rowData = row.getItem();
                if (event.getClickCount() == 1 ) {
                    System.out.println("Selected Plane: " + rowData.toString());
                    selectedPlane = rowData;
                    //Todo save change in PersistanceLayer
                }
            }
        });
        planeTablePane.getChildren().add(planeTable);

    }

    private void updatePlanes(String term){
        String lowerTerm = term.toLowerCase();
        selectedPlanes = App.businessLogicAPI.getAllPlanes(plane -> {
            if (plane.toString().toLowerCase().contains(lowerTerm)) {
                return true;
            }
            return false;
        });
        createPlaneTableWithData(selectedPlanes);

    }

    @FXML
    void exit() throws IOException {
        setRoot("home");
    }

    @FXML
    void saveFlight(ActionEvent event) throws IOException {
        var price = Double.valueOf(flightPrice.getText());
        var flightNumber = Integer.valueOf(flightNumberTextField.getText());
        var plane = selectedPlane;
        var creator = (SalesOfficer) App.employee;                  // ToDo: verify that only officer can register new flights

        // content of previous scene
        var extendedRoute = CreateFlightController.getExtendedRoute();
        var route = extendedRoute.getSelectedRoute();
        var depDateTime = extendedRoute.getDepartureDateWithTime();
        var arrDateTime = extendedRoute.getArrivalDateWithTime();

        var flightCreated = App.businessLogicAPI.createFlightFromUI(creator, flightNumber, depDateTime, arrDateTime,route, plane, price);
        if(flightCreated) {
            exit();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving flight");
            alert.setContentText("There was an error while saving the created flight. Try again!");
            alert.showAndWait();
        }

    }

}
