package com.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.gui.RouteTable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
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
        boolean inputOK = true;
        try{
            extendedRoute = new ExtendedRoute(getStartDate(), getStartTime(), getDurationHours(), getDurationMinutes());
            extendedRoute.setRoute(getSelectedRoute());
            setRoot("submitFlight");
        }catch (Exception e){
            inputOK = false;
            e.printStackTrace();
        }

        // After "saving" current selections
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

                this.departureDateWithTime = createDepartureInfo(startDate, startTime);


                this.arrivalDateWithTime = createArrivalInfo(durationHours, durationMinutes);

        }

        private void setRoute(Route r) throws IllegalArgumentException{
            if(r == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error during registration");
                alert.setContentText("There is no selected Route");
                alert.showAndWait();

            }
            this.selectedRoute = r;
        }

        //helper
        private LocalDateTime createDepartureInfo(LocalDate startDate, TextField startTime) throws IllegalArgumentException{
            String[] splittedField = startTime.getText().split(":");

            if(splittedField.length > 0){
                if(splittedField.length == 2) {
                    int hour = -1;
                    int min = -1;
                    boolean inputOK = true;
                    try{
                        hour = Integer.valueOf(splittedField[0].trim());
                        min = Integer.valueOf(splittedField[1].trim());
                    } catch(Exception e) {
                        if(e.getClass().equals(NumberFormatException.class)){
                            inputOK = false;
                        }
                    }

                    if(inputOK){
                        return startDate.atTime(hour, min);
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error during registration");
                    alert.setContentText("The entered information regarding the departure are either not filled in or filled in wrongly." +
                            " Make sure the input looks like 'hh:mm'.");
                    alert.showAndWait();
                    new IllegalArgumentException().printStackTrace();
                }

            }

            throw new IllegalArgumentException("Wrong input-syntax. input must look like: 'hh:mm'.");
        }

        private LocalDateTime createArrivalInfo(TextField durHour, TextField durMin) throws InputMismatchException{
            int hours = -1;
            int mins = -1;
            if(durHour.getText().isEmpty() || durMin.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error during registration");
                alert.setContentText("Please make sure that no fields are empty.");
                alert.showAndWait();
                throw new InputMismatchException("Wrong input-syntax. input must look like: 'hh:mm'.");
            }else{
                try{
                    hours = Integer.valueOf(durHour.getText().trim());
                    mins = Integer.valueOf(durMin.getText().trim());
                }catch (Exception e){
                        throw new InputMismatchException("Wrong input-syntax. input must look like: 'hh:mm'.");

                }
            }

            return this.departureDateWithTime.plusHours(hours).plusMinutes(mins);
        }

        //getter of inner-class
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