package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Route;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.RouteImpl;
import com.g02.flightsalesfx.helpers.Bundle;
import com.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewKPIsController implements Controller {

    @FXML
    private AnchorPane routePane;

    @FXML
    private ListView<Route> routeListView;

    @FXML
    private TextField searchTextField;

    @FXML
    public void initialize() {
        var routes = App.businessLogicAPI.getAllRoutes( route -> true);
        routeListView.getItems().addAll(routes);
//        routeListView.setMinWidth(routePane.getPrefWidth());

        searchTextField.textProperty().addListener( ((observableValue, oldValue, newValue) -> {
            var filteredRoutes = routes.stream()
                    .filter( route -> {
                        String airportString = (route.getDepartureAirport().getName() +
                                route.getArrivalAirport().getName()).toLowerCase().trim();
                        return airportString.contains(newValue.toLowerCase().trim());
                    })
                    .collect(Collectors.toList());
            routeListView.getItems().clear();
            routeListView.getItems().addAll(filteredRoutes);
        }));
    }

    @FXML
    void viewButton() {
        //DO SOMETHING
        var route = routeListView.getSelectionModel().getSelectedItem();
        if (route == null) {
            Alert.AlertType alertAlertType;
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No route selected.");
            alert.setContentText("Please select a route to continue!");
            alert.showAndWait();
        } else {
            Bundle bundle = new Bundle();
            bundle.add("route", route);
            App.setRoot("viewSpecificRoutesKPIs", bundle);
        }
    }

    @FXML
    void backToMenu() {
        App.setRoot("managementDashboard");
    }
}
