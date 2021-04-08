package com.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.gui.RouteTable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void initialize() {
        selectedRoutes = App.businessLogicAPI.getAllRoutes(route -> {
            return route.getEnabled();
        });

        routeSearchBar.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                updateRoutes(newValue);
        }));

        routeTable = new RouteTable(selectedRoutes, (event, row) -> {
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

    private void updateRoutes(String term){
        selectedRoutes = App.businessLogicAPI.getAllRoutes(route -> {
            return route.getEnabled()&&route.toString().toLowerCase().contains(term.toLowerCase());
        });
        if(routeTable != null)
            routeTable.refresh();
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
    void nextStep() throws IOException{



        // After "saving" current selections
        setRoot("submitPlane"); // todo: select correct fxml
    }

    //getter


    public DatePicker getStartDate() {
        return startDate;
    }

    public TextField getStartTime() {
        return startTime;
    }

    public TextField getDurationHours() {
        return durationHours;
    }

    public TextField getDurationMinutes() {
        return durationMinutes;
    }

    //to save route + dateinfos for following flight creation
    public class ExtendedRoute {

        private Route selectedRoute;
        private LocalDateTime departureDateWithTime;
        private LocalDateTime arrivalDateWithTime;

        public ExtendedRoute(LocalDate startDate, TextField startTime, TextField durationHours, TextField durationMinutes) {

        }

        //helper
        private LocalDateTime createDepartureInfo(LocalDate startDate, TextField startTime) {
            String[] splittedField = startTime.toString().split(":");

            if(splittedField.length == 2) {
                int hour = Integer.valueOf(splittedField[0]);
                int min = Integer.valueOf(splittedField[1]);

                return startDate.atTime(hour, min);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
