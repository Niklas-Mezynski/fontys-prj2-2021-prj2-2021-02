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

        optionList.setCellFactory( lv -> {
            ListCell<FlightOption> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();


//            MenuItem editItem = new MenuItem();
//            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
//            editItem.setOnAction(event -> {
//                String item = cell.getItem();
//                // code to edit item...
//            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete flight option"));
            //TODO Delete the FlightOption
            deleteItem.setOnAction(event -> System.out.println("Trying to delete: " + cell.getItem().toString()));
            contextMenu.getItems().addAll( deleteItem );

            cell.textProperty().bind(cell.itemProperty().asString());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            if (cell == null) {
                System.out.println("huhn");
            }

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

    @FXML
    void save() throws IOException {
        exit();
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
