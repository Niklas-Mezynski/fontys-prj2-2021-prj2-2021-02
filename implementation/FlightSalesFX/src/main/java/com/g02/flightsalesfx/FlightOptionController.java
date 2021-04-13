package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.FlightOption;
import com.g02.flightsalesfx.businessLogic.FlightOptionImpl;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.g02.flightsalesfx.App.setRoot;

public class FlightOptionController {

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

        optionList.setCellFactory( lv -> {
            ListCell<FlightOption> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete flight option"));
            //TODO Delete the FlightOption
            deleteItem.setOnAction(event -> {
                System.out.println("Trying to delete: " + cell.getItem().toString());
                selectedFlight.getFlightOptions().remove(cell.getItem());
                updateListView();
            });
            contextMenu.getItems().addAll( deleteItem );

            cell.textProperty().bind(cell.itemProperty().asString());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });

            System.out.println("List size: " + optionList.getItems().size());

            return cell ;
        });

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
            if (i > 0) {
                return true;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return false;
    }


}
