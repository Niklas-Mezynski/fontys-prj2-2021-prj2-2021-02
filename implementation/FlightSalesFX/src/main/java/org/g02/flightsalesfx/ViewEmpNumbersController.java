package org.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.g02.flightsalesfx.businessEntities.Employee;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessEntities.SalesEmployee;
import org.g02.flightsalesfx.businessLogic.SalesEmployeeImpl;
import org.g02.flightsalesfx.gui.EmployeeTable;
import org.g02.flightsalesfx.gui.RouteTable;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ViewEmpNumbersController implements Controller {

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField searchTextField;

    @FXML
    private AnchorPane tablePane;

    private EmployeeTable empTable;
    private Employee selectedEmployee;

    @FXML
    void initialize() {
        List<Employee> employees = App.businessLogicAPI.getAllEmployees(employee -> true);
        List<Employee> salesEmployees = employees.stream()
                .filter(emp -> emp instanceof SalesEmployee)
                .map(emp -> SalesEmployeeImpl.of((SalesEmployee) emp))
                .collect(Collectors.toList());
//        empListView.getItems().addAll(salesEmployees);
////        routeListView.setMinWidth(routePane.getPrefWidth());
//
//        searchTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
//            var filteredEmployees = salesEmployees.stream()
//                    .filter(emp -> emp.getName().contains(newValue) || emp.getEmail().contains(newValue))
//                    .collect(Collectors.toList());
//            empListView.getItems().clear();
//            empListView.getItems().addAll(filteredEmployees);
//        }));
        createOrUpdateEmpTable(salesEmployees, employee -> true);

        searchTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            Predicate<Employee> pred = (emp -> emp.getName().toLowerCase().contains(newValue.toLowerCase()) || emp.getEmail().toLowerCase().contains(newValue.toLowerCase()));
            createOrUpdateEmpTable(salesEmployees, pred);
        }));


    }

    public void createOrUpdateEmpTable(List<Employee> allSalesEmps, Predicate<Employee> pr) {
        tablePane.getChildren().remove(empTable);

        var filteredEmps = allSalesEmps.stream().filter(pr).collect(Collectors.toList());

        empTable = new EmployeeTable(filteredEmps, (event, row) -> {
            if (!row.isEmpty()) {
                selectedEmployee = row.getItem();
            }
        });
        tablePane.getChildren().add(empTable);
        empTable.setMinWidth(pane.getPrefWidth());
    }

    @FXML
    void backToMenu() {
        App.setRoot("managementDashboard");
    }

    @FXML
    void viewButton() {
        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No employee selected.");
            alert.setContentText("Please select an employee to continue!");
            alert.showAndWait();
        } else {
            Bundle bundle = new Bundle();
            bundle.add("emp", selectedEmployee);
            App.setRoot("viewSpecEmpNumbers", bundle);
        }
    }
}
