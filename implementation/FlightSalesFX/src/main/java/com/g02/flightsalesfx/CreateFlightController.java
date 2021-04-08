package com.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.gui.RouteTable;

import java.io.IOException;

import static com.g02.flightsalesfx.App.setRoot;

public class CreateFlightController {

    //private Route selectedRoute;

    @FXML
    private TextField routeSearchBar;

    @FXML
    private Button createRouteButton;

    @FXML
    private Button saveFlightButton;

    @FXML
    private Button exitFlightButton;

    public void initialize() {
        var selectedRoutes = App.businessLogicAPI.getAllRoutes(route -> {
            return route.getEnabled();
        });

        routeSearchBar.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue.length() != 0){

            }else{

            }
        }));



        var routeTable = new RouteTable(selectedRoutes, (event, row) -> {
            if (!row.isEmpty()) {
                Route rowData = row.getItem();
                if (event.getClickCount() == 2 && event.isControlDown()) {
                    System.out.println("Ctrl + click on: " + rowData.toString() + rowData.getEnabled());
                    rowData.toggleEnable();
                    row.getTableView().refresh();
                    //Todo save change in PersistanceLayer
                }
            }
        });
    }

    @FXML
    private void createRoute() throws IOException {
        setRoot("createRoute");
    }

    @FXML
    private void exit() throws IOException {
        setRoot("home");
    }

    @FXML
    void saveFlight() {
        // in progress
    }

}
