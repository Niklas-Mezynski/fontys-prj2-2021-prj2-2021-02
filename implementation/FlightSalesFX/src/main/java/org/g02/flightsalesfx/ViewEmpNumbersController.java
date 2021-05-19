package org.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.g02.flightsalesfx.businessEntities.Employee;
import org.g02.flightsalesfx.businessEntities.SalesEmployee;
import org.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;

import java.util.List;
import java.util.stream.Collectors;

public class ViewEmpNumbersController implements Controller {

    @FXML
    private TextField searchTextField;

    @FXML
    private ListView<SalesEmployeeImpl> empListView;

    @FXML
    void initialize() {
        List<Employee> employees = App.businessLogicAPI.getAllEmployees(employee -> true);
        List<SalesEmployeeImpl> salesEmployees = employees.stream()
                .filter(emp -> emp instanceof SalesEmployee)
                .map(emp -> SalesEmployeeImpl.of((SalesEmployee) emp))
                .collect(Collectors.toList());
        empListView.getItems().addAll(salesEmployees);
//        routeListView.setMinWidth(routePane.getPrefWidth());

        searchTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            var filteredEmployees = salesEmployees.stream()
                    .filter(emp -> emp.getName().contains(newValue) || emp.getEmail().contains(newValue))
                    .collect(Collectors.toList());
            empListView.getItems().clear();
            empListView.getItems().addAll(filteredEmployees);
        }));
    }

    @FXML
    void backToMenu() {
        App.setRoot("managementDashboard");
    }

    @FXML
    void viewButton() {
        var emp = empListView.getSelectionModel().getSelectedItem();
        if (emp == null) {
            Alert.AlertType alertAlertType;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No employee selected.");
            alert.setContentText("Please select an employee to continue!");
            alert.showAndWait();
        } else {
            Bundle bundle = new Bundle();
            bundle.add("emp", emp);
            App.setRoot("viewSpecEmpNumbers", bundle);
        }
    }
}
