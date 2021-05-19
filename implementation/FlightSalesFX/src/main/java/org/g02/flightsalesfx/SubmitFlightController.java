package org.g02.flightsalesfx;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.converter.CurrencyStringConverter;
import org.g02.flightsalesfx.businessEntities.Airport;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.SalesOfficer;
import org.g02.flightsalesfx.businessLogic.FlightImpl;
import org.g02.flightsalesfx.businessLogic.FlightOptionImpl;
import org.g02.flightsalesfx.businessLogic.SalesOfficerImpl;
import org.g02.flightsalesfx.gui.PlaneTable;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import static org.g02.flightsalesfx.App.setRoot;

public class SubmitFlightController implements Controller {

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

    @FXML
    private VBox flightOptionsContainer;

    @FXML
    private Button addFlightOptionBT;

    @FXML
    private CheckBox checkboxSalesprocess;

    private List<Plane> selectedPlanes;
    private PlaneTable planeTable;
    private Plane selectedPlane = null;

    @FXML
    void addFlightOption(){
        //construct new FlightOptionHBOX
        var fohbox=new HBox();

        var namefield=new TextField();
        namefield.setPromptText("Name");
        namefield.setTooltip(new Tooltip("The Name that this Flight Option should have"));
        namefield.setPrefWidth(100);
        fohbox.getChildren().add(namefield);

        var availableLabel= new Label("Available:");
        availableLabel.setPrefWidth(60);
        fohbox.getChildren().add(availableLabel);

        var availableSpinner=new Spinner<Integer>();
        availableSpinner.setPrefWidth(70);
        availableSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        availableSpinner.setEditable(true);
        fohbox.getChildren().add(availableSpinner);

        var surchargeField=new TextField();
        surchargeField.setPromptText("Surcharge");
        namefield.setTooltip(new Tooltip("The Surcharge that selecting this Flight Option should add to the Price"));
        surchargeField.setPrefWidth(80);
        surchargeField.setPrefHeight(Region.USE_COMPUTED_SIZE);
        fohbox.getChildren().add(surchargeField);

        var buttonsContainer=new GridPane();
        buttonsContainer.setMaxHeight(surchargeField.getPrefHeight());
        buttonsContainer.setPrefHeight(surchargeField.getPrefHeight());

        var removeButton=new Button("X");
        removeButton.setOnAction(e->{
            flightOptionsContainer.getChildren().remove(fohbox);
            if(flightOptionsContainer.getChildren().size()==0){
                addFlightOptionBT.setVisible(true);
            }else {
                //enable add button on last FlightOptionBox
                var last=(HBox)flightOptionsContainer.getChildren().get(flightOptionsContainer.getChildren().size()-1);
                var lastBtContainer=(GridPane)last.getChildren().get(4);
                lastBtContainer.getChildren().get(1).setVisible(true);
            }
        });
        removeButton.setMaxHeight(buttonsContainer.getMaxHeight()/2);
        removeButton.setPrefHeight(buttonsContainer.getPrefHeight()/2);
        removeButton.setStyle("-fx-font-size: 7");

        buttonsContainer.add(removeButton,0,0);

        var addButton=new Button("+");
        addButton.setOnAction(e->{
            addFlightOption();
            addButton.setVisible(false);
        });
        addButton.setMaxHeight(buttonsContainer.getMaxHeight()/2);
        addButton.setPrefHeight(buttonsContainer.getPrefHeight()/2);
        addButton.setStyle("-fx-font-size: 7");

        buttonsContainer.add(addButton,0,1);

        fohbox.getChildren().add(buttonsContainer);

        flightOptionsContainer.getChildren().add(fohbox);
        addFlightOptionBT.setVisible(false);
    }

    static public boolean helperIsPlaneAvailable(List<Flight> allFlights,Plane searchedPlane, LocalDateTime searchedTime, Airport searchAirport) {
        var flightsArr = allFlights.stream()
                .filter(s -> s.getPlane().equals(searchedPlane))
                .collect(Collectors.toList());
        // clone the other list if you want to manipulate them independently, but since only stream operations are done on them,
        // they can be identical (they could also use the same variable)

        return !flightsArr.stream()
                .anyMatch(s->{
                    return s.getDeparture().isBefore(searchedTime)&&s.getArrival().isAfter(searchedTime);
                });
    }

    public void initialize() {
        selectedPlanes = App.businessLogicAPI.getAllPlanes(route -> true);

        planeSearchBar.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            updatePlanes(newValue);
        }));

        createPlaneTableWithData(selectedPlanes);


    }

    private void createPlaneTableWithData(List<Plane> planes) {
        planeTablePane.getChildren().remove(planeTable);
        var extendedRoute = CreateFlightController.getExtendedRoute();
        var flights=App.businessLogicAPI.getAllFlights(b->true); //Get and cache before killing the backend with SQL-requests (taking O(n) time based on the number of total planes)
        planes = planes.stream().filter(s -> helperIsPlaneAvailable(flights, s, extendedRoute.getDepartureDateWithTime(), extendedRoute.getSelectedRoute().getDepartureAirport())).collect(Collectors.toList());
        planeTable = new PlaneTable(planes, (event, row) -> {
            if (!row.isEmpty()) {
                Plane rowData = row.getItem();
                if (event.getClickCount() == 1) {
                    System.out.println("Selected Plane: " + rowData.toString());
                    selectedPlane = rowData;
                }
            }
        });
        planeTablePane.getChildren().add(planeTable);

    }

    private void updatePlanes(String term) {
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
        double price = -1;
        boolean conversionOK = true;
        try {
            price = Double.parseDouble(flightPrice.getText().replace(",",".").replaceAll("[€$]",""));
        } catch (Exception e) {
            if (e.getClass().equals(NumberFormatException.class)) {
                conversionOK = false;
            }
        }

        var plane = selectedPlane;
        var creator = (SalesOfficer) App.employee;                  // ToDo: verify that only officer can register new flights

        // content of previous scene
        var extendedRoute = CreateFlightController.getExtendedRoute();
        var route = extendedRoute.getSelectedRoute();
        var depDateTime = extendedRoute.getDepartureDateWithTime();
        var arrDateTime = extendedRoute.getArrivalDateWithTime();

        if (creator != null && depDateTime != null && arrDateTime != null && route != null && plane != null && price != -1 && conversionOK) {

            var flightOptionList=flightOptionsContainer.getChildren().stream()
                    .map(n->(HBox)n)
                    .map(box->{
                        var nameTF=(TextField)box.getChildren().get(0);
                        var name=nameTF.getText();
                        var availableSP=(Spinner<Integer>)box.getChildren().get(2);
                        var availabel=availableSP.getValue();
                        var surchargeTF=(TextField)box.getChildren().get(3);
                        var surchargeString=surchargeTF.getText();
                        surchargeString=surchargeString.replace(",",".");
                        surchargeString=surchargeString.replaceAll("[€$]","");
                        var surchargeValue=Double.parseDouble(surchargeString);
                        return new FlightOptionImpl(name,availabel,surchargeValue);
                    })
                    .collect(Collectors.toList());
            System.out.println(flightOptionList);
            var newFlight = new FlightImpl((SalesOfficerImpl) creator, depDateTime, arrDateTime, route, plane, price);
            newFlight.addAllFlightOptions(flightOptionList);
            if(checkboxSalesprocess.isSelected()) {
                newFlight.startSalesProcess();
            }
            //var flightCreated = App.businessLogicAPI.createFlightFromUI(creator, depDateTime, arrDateTime, route, plane, price, flightOptionList);
            var flightCreated = App.businessLogicAPI.createFlightFromUI(newFlight);

            if (flightCreated) {

                exit();
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error saving flight");
                alert.setContentText("There was an error while saving the created flight. Try again!");
                alert.showAndWait();

            }

        } else {
            System.out.println("Alarm");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Check your inputs");
            alert.setContentText("Check, that all inputs have correct values and a plane is selected.");
            alert.showAndWait();

        }
    }
}
