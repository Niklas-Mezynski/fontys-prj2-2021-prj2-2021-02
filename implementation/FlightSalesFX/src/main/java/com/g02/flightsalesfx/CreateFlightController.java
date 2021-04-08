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
    private Route selectedRoute = null;

    private static ExtendedRoute extendedRoute;

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
                    System.out.println("Selected Route: " + rowData.toString());
                    selectedRoute = rowData;
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

        App.comesFromCreateFlight = true;
        setRoot("createRoute");
    }

    @FXML
    private void exit() throws IOException {
        setRoot("home");
    }

    @FXML
    void nextStep() throws IOException {
        //"save" latest selections and verify that input is correct (syntax)
        extendedRoute = new ExtendedRoute(getStartDate(), getStartTime(), getDurationHours(), getDurationMinutes());

        // After "saving" current selections
        setRoot("submitFlight");
    }

    //getter for extendedRoute that is necessary for further flight-creation
    public static ExtendedRoute getExtendedRoute() {
        return extendedRoute;
    }

    //general getters
    private LocalDate getStartDate() {
        return startDate.getValue();
    }

    private TextField getStartTime() {
        return startTime;
    }

    private TextField getDurationHours() {
        return durationHours;
    }

    private TextField getDurationMinutes() {
        return durationMinutes;
    }

    private Route getSelectedRoute() {
        return selectedRoute;
    }

    //to save route + dateinfos for following flight creation
    public class ExtendedRoute {

        private Route selectedRoute;
        private LocalDateTime departureDateWithTime;
        private LocalDateTime arrivalDateWithTime;

        public ExtendedRoute(LocalDate startDate, TextField startTime, TextField durationHours, TextField durationMinutes) {
            if ( getSelectedRoute() ==null ) {
                //todo: add alert
                System.out.println("ALARM");
            }

            this.selectedRoute = getSelectedRoute();
            this.departureDateWithTime = createDepartureInfo(startDate, startTime);
            this.arrivalDateWithTime = createArrivalInfo(durationHours, durationMinutes);
        }

        //helper
        private LocalDateTime createDepartureInfo(LocalDate startDate, TextField startTime) {
            String[] splittedField = startTime.getText().split(":");

            if(splittedField.length == 2) {
                int hour = Integer.valueOf(splittedField[0]);
                int min = Integer.valueOf(splittedField[1]);

                return startDate.atTime(hour, min);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error during registration");
                alert.setContentText("The entered information regarding the departure are either not filled in or filled in wrongly.");
                alert.showAndWait();
            }
            return null;
        }

        private LocalDateTime createArrivalInfo(TextField durHour, TextField durMin) {
            int hours = Integer.valueOf(durHour.getText());
            int mins = Integer.valueOf(durMin.getText());

            return this.departureDateWithTime.plusHours(hours).plusMinutes(mins);
        }

        public Route getSelectedRoute() {
            return selectedRoute;
        }

        public LocalDateTime getDepartureDateWithTime() {
            return departureDateWithTime;
        }

        public LocalDateTime getArrivalDateWithTime() {
            return arrivalDateWithTime;
        }
    }
}