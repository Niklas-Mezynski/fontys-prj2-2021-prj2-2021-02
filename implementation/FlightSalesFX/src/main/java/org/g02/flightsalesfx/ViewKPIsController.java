package org.g02.flightsalesfx;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessLogic.AirportImpl;
import org.g02.flightsalesfx.businessLogic.RouteImpl;
import org.g02.flightsalesfx.gui.RouteTable;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ViewKPIsController implements Controller {

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField searchTextField;

    @FXML
    private AnchorPane listPane;

    private RouteTable routeTable;

    private Route selectedRoute;

    @FXML
    public void initialize() {
        var routes = App.businessLogicAPI.getAllRoutes(route -> true);
        createOrUpdateRouteTable(routes, route -> true);

        searchTextField.textProperty().addListener( ((observableValue, oldValue, newValue) -> {
            Predicate<Route> pred = ( route -> {
                String airportString = (route.getDepartureAirport().getName() +
                        route.getArrivalAirport().getName()).toLowerCase().trim();
                return airportString.contains(newValue.toLowerCase().trim());
            });
            createOrUpdateRouteTable(routes, pred);
        }));


    }

    public void createOrUpdateRouteTable(List<Route> allRoutes, Predicate<Route> pr) {
        listPane.getChildren().remove(routeTable);

        var filteredRoutes = allRoutes.stream().filter(pr).collect(Collectors.toList());

        routeTable = new RouteTable(filteredRoutes, (event, row) -> {
            if (!row.isEmpty()) {
                selectedRoute = row.getItem();
            }
        });
        listPane.getChildren().add(routeTable);
        routeTable.setMinWidth(pane.getPrefWidth());
    }

    @FXML
    void viewButton() {
        //DO SOMETHING
        if (selectedRoute == null) {
            Alert.AlertType alertAlertType;
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No route selected.");
            alert.setContentText("Please select a route to continue!");
            alert.showAndWait();
        } else {
            Bundle bundle = new Bundle();
            bundle.add("route", selectedRoute);
            App.setRoot("viewSpecificRoutesKPIs", bundle);
        }
    }

    @FXML
    void backToMenu() {
        App.setRoot("managementDashboard");
    }
}
