package com.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.gui.RouteTable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.g02.flightsalesfx.App.setRoot;

public class CreateFlightController {

    //private Route selectedRoute;

    @FXML
    private TextField routeSearchBar;

    @FXML
    private AnchorPane routeTablePane;

    @FXML
    private Button createRouteButton;

    @FXML
    private DatePicker startDate;

    @FXML
    private TextField startTime;

    @FXML
    private TextField durationHours;

    @FXML
    private TextField durationMinutes;

    @FXML
    private Button nextStepButton;

    @FXML
    private Button exitFlightButton;




    private List<Route> selectedRoutes;
    private RouteTable routeTable;
    private Route selectedRoute = null;

    public void initialize() {
        selectedRoutes = App.businessLogicAPI.getAllRoutes(route -> {
            return route.getEnabled();
        });

        routeSearchBar.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                updateRoutes(newValue);
        }));

        createRouteTableWithData(selectedRoutes);


    }

    private void createRouteTableWithData(List<Route> routes){
        routeTablePane.getChildren().remove(routeTable);
        routeTable = new RouteTable(routes, (event, row) -> {
            if (!row.isEmpty()) {
                Route rowData = row.getItem();
                if (event.getClickCount() == 1 ) {
                    System.out.println("Ctrl + click on: " + rowData.toString() + rowData.getEnabled());
                    rowData.toggleEnable();
                    row.getTableView().refresh();

                    //Todo save change in PersistanceLayer
                }
            }
        });
        routeTablePane.getChildren().add(routeTable);

    }

    private void updateRoutes(String term){
        String lowerTerm = term.toLowerCase();
        selectedRoutes = App.businessLogicAPI.getAllRoutes(route -> {
            String[] terms = lowerTerm.split(" ");
            for(String s : terms){
                if(!(route.getEnabled()&&route.toString().toLowerCase().contains(s))){
                    return false;
                }
            }


            return true;
        });
        if(routeTable != null){
            createRouteTableWithData(selectedRoutes);
        }

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
    void nextStep() {
        
    }


}
