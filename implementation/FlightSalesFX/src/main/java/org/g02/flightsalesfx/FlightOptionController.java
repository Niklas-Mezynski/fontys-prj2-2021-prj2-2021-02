package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.FlightOption;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static org.g02.flightsalesfx.App.setRoot;

public class FlightOptionController implements Controller {

    @FXML
    private Label flightNumberLabel;

    @FXML
    private TextField planeInfoText;

    @FXML
    private VBox mainVBox;

    @FXML
    private ListView<FlightOption> optionList;

    @FXML
    private TextField optionName;

    @FXML
    private TextField optionMaxAvailable;

    @FXML
    private TextField optionPrice;

    private Flight selectedFlight;

    public void initialize () throws IOException {
        selectedFlight = EditFlightController.selectedFlight;
        if (selectedFlight == null) {
            App.setRoot("home");
            return;
        }
        flightNumberLabel.setText("Edit Flight Options for flight number: " + selectedFlight.getFlightNumber());
        planeInfoText.setText("Seats: " + selectedFlight.getPlane().getSeatCount());


        optionMaxAvailable.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (isIntegerGreater0(optionMaxAvailable.getText())) {
                optionMaxAvailable.setStyle("-fx-border-color:none;");
            } else {
                optionMaxAvailable.setStyle("-fx-border-color:red;");
            }
        }));

        optionPrice.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (isNumeric(optionPrice.getText())) {
                optionPrice.setStyle("-fx-border-color:none;");
            } else {
                optionPrice.setStyle("-fx-border-color:red;");
            }
        }));


        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem();
        deleteItem.textProperty().bind(Bindings.format("Delete flight option"));
        //TODO Delete the FlightOption
        deleteItem.setOnAction(event -> {
            System.out.println("Trying to delete: " + optionList.getSelectionModel().getSelectedItem().toString());
            selectedFlight.getFlightOptions().remove(optionList.getSelectionModel().getSelectedItem());
            updateListView();
        });
        contextMenu.getItems().addAll( deleteItem );
        optionList.setContextMenu(contextMenu);

        updateListView();

    }

    @FXML
    void addFlightOption() {
        int maxAv = 0;
        if (isIntegerGreater0(optionMaxAvailable.getText())) {
            maxAv = Integer.parseInt(optionMaxAvailable.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding the FlightOption");
            alert.setContentText("Input must be an Integer");
            alert.showAndWait();
            return;
        }
        double optionPriceDouble = 0;
        if (isNumeric(optionPrice.getText())) {
            optionPriceDouble = Double.parseDouble(optionPrice.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding the FlightOption");
            alert.setContentText("Input must be numeric");
            alert.showAndWait();
            return;
        }

        App.businessLogicAPI.addFlightOptionFromUI(
                optionName.getText(),
                maxAv,
                optionPriceDouble,
                selectedFlight
        );
        updateListView();
    }

    @FXML
    void exit() throws IOException {
        selectedFlight = null;
        setRoot("home");
    }

    private void updateListView() {
        var flightOptions = selectedFlight.getFlightOptions();
        optionList.getItems().clear();
        optionList.getItems().addAll(flightOptions);
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean isIntegerGreater0(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
            if (i > 0 && i <= selectedFlight.getPlane().getSeatCount()){
                return true;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return false;
    }


}
