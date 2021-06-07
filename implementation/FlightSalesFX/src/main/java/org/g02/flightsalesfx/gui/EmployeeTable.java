package org.g02.flightsalesfx.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.g02.flightsalesfx.businessEntities.Employee;
import org.g02.flightsalesfx.businessEntities.Route;
import org.g02.flightsalesfx.businessEntities.SalesEmployee;

import java.util.List;

public class EmployeeTable extends TableView<SalesEmployee> {

    public EmployeeTable (List<SalesEmployee> employees, RowClickHandler<SalesEmployee> clickHandler) {

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn mailColumn = new TableColumn("Email");
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getItems().addAll(employees);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getColumns().addAll(nameColumn, mailColumn);
        setRowFactory(rteTableView -> {
            TableRow<SalesEmployee> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                clickHandler.click(mouseEvent, row);
            });
            return row ;
        });
    }
}
