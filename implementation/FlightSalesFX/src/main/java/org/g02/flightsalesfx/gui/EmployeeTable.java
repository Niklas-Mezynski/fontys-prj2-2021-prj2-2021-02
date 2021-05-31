package org.g02.flightsalesfx.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.g02.flightsalesfx.businessEntities.Employee;
import org.g02.flightsalesfx.businessEntities.Route;

import java.util.List;

public class EmployeeTable extends TableView<Employee> {

    public EmployeeTable (List<Employee> employees, RowClickHandler<Employee> clickHandler) {

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn mailColumn = new TableColumn("Email");
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getItems().addAll(employees);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(nameColumn, mailColumn);
        setRowFactory(rteTableView -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row ;
        });
    }
}
