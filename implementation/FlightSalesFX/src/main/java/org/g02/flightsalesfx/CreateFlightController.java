package org.g02.flightsalesfx;

import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.gui.RouteTable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

import static org.g02.flightsalesfx.App.setRoot;

public class CreateFlightController implements Controller {

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



    private List<Route> allRoutesCache;
    private List<Route> selectedRoutes;
    private RouteTable routeTable;
    private Route selectedRoute = null;

    private static ExtendedRoute extendedRoute;

    public void initialize() {
        allRoutesCache = App.businessLogicAPI.getAllRoutes(Route::getEnabled);
        selectedRoutes=new ArrayList<>(allRoutesCache);
        routeSearchBar.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                updateRoutes(newValue);
        }));

        createRouteTableWithData(selectedRoutes);

        routeSearchBar.setTooltip(new Tooltip("Search for a route"));
        startDate.setTooltip(new Tooltip("Select the date on which the Flight should depart"));
        startTime.setTooltip(new Tooltip("The time at which the Flight departs, in the hh:mm format e.g. \"13:12\""));
        durationHours.setTooltip(new Tooltip("The hours part of the duration that this Flight takes, whole Numbers only"));
        durationMinutes.setTooltip(new Tooltip("The minutes part of the duration that this Flight takes, whole Numbers only, has to be smaller than 60"));

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
        selectedRoutes = allRoutesCache.stream().filter(route -> {
            String[] terms = lowerTerm.split(" ");
            for(String s : terms){
                if(!(route.getEnabled()&&route.toString().toLowerCase().contains(s))){
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
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

                try {
                    this.arrivalDateWithTime = createArrivalInfo(durationHours, durationMinutes);
                } catch (InputMismatchException | IllegalArgumentException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error during registration");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    throw e;
                }
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
                    try{
                        hour = Integer.parseInt(splittedField[0].trim());
                        min = Integer.parseInt(splittedField[1].trim());
                        return startDate.atTime(hour, min);
                    } catch(NumberFormatException e) {
                        throw new IllegalArgumentException("The entered information regarding the departure is either not filled in or filled in wrongly." +
                                " Make sure the Departure Time looks like this: 'hh:mm'.");
                    }
                } else {
                    throw new IllegalArgumentException("The entered information regarding the departure is either not filled in or filled in wrongly." +
                            " Make sure the Departure Time looks like this: 'hh:mm'.");
                }

            }

            throw new IllegalArgumentException("Wrong input-syntax. input must look like: 'hh:mm'.");
        }

        private LocalDateTime createArrivalInfo(TextField durHour, TextField durMin) throws InputMismatchException{
            int hours;
            int mins;
            if(durHour.getText().isEmpty() || durMin.getText().isEmpty()){
                throw new InputMismatchException("Please make sure that no fields are empty");
            }else{
                try{
                    hours = Integer.parseInt(durHour.getText().trim());
                    mins = Integer.parseInt(durMin.getText().trim());
                    if(mins>=60||mins<0||hours<0){
                        throw new InputMismatchException("Tha values for the Flight-Duration are outside of the valid range");
                    };
                }catch (NumberFormatException e){
                        throw new InputMismatchException("Wrong input-syntax. Duration has to be a valid number");
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