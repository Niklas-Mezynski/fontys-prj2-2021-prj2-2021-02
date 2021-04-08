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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.List;

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
            String[] terms = lowerTerm.split(" ");
            for(String s : terms){
                if (plane.toString().toLowerCase().contains(s)) {
                    return true;
                }
            }

            return true;
        });
//        if(planeTable != null){
            createPlaneTableWithData(selectedPlanes);
//        }

    }

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void saveFlight(ActionEvent event) {
        var price = Double.valueOf(flightPrice.getText());
        var flightNumber = 123;                                     // ToDo
        var plane = selectedPlane;
        var creator = (SalesOfficer) App.employee;                  // ToDo: verify that only officer can register new flights

        // content of previous scene
        var extendedRoute = CreateFlightController.getExtendedRoute();
        var route = extendedRoute.getSelectedRoute();
        var depDateTime = extendedRoute.getDepartureDateWithTime();
        var arrDateTime = extendedRoute.getArrivalDateWithTime();

        var flightCreated = App.businessLogicAPI.createFlightFromUI(creator, flightNumber, depDateTime, arrDateTime,route, plane, price);
    }

}
