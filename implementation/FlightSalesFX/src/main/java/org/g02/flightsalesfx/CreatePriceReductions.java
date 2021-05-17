package org.g02.flightsalesfx;


import javafx.scene.layout.VBox;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.PriceReduction;
import org.g02.flightsalesfx.businessLogic.PriceReductionImpl;
import org.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl;
import org.g02.flightsalesfx.gui.FlightTable;
import org.g02.flightsalesfx.gui.PriceReductionsTable;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.g02.flightsalesfx.persistence.PriceReductionStorageService;
import org.g02.flightsalesfx.persistence.PriceReductionStorageServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreatePriceReductions implements Controller {

    @FXML
    public DatePicker startDate;
    public DatePicker endDate;
    public TextField redPrice;
    public TextField redName;
    public ComboBox<String> startMin;
    public ComboBox<String> endMin;
    public ComboBox<String> endHour;
    public ComboBox<String> startHour;
    public CheckBox isPercent;
    public Button addReduction;
    public VBox flightVBox;
    public VBox reductionsVBox;

    private void setData(){
        List<String> hourTimes=new ArrayList<>();
        for(int i=0;i<24;i++){
            if(i<10){
                hourTimes.add("0"+i);
            }
            else{
                hourTimes.add(""+i);
            }
        }
        List<String> minTimes=new ArrayList<>();
        for(int i=0;i<60;i++){
            if(i<10){
                minTimes.add("0"+i);
            }
            else{
                minTimes.add(""+i);
            }
        }
        startHour.getItems().addAll(hourTimes);
        endHour.getItems().addAll(hourTimes);
        startMin.getItems().addAll(minTimes);
        endMin.getItems().addAll(minTimes);
        startHour.setVisibleRowCount(5);
        endHour.setVisibleRowCount(5);
        startMin.setVisibleRowCount(5);
        endMin.setVisibleRowCount(5);
    }
    public void initialize() {
        final var allFlights=App.businessLogicAPI.getAllFlights();
        final var allReductions=App.businessLogicAPI.getAllPriceReductions();
        addReduction.setOnMouseClicked((event)->{
            LocalDateTime end=endDate.getValue().atTime(Integer.parseInt(endHour.getValue()),Integer.parseInt(endMin.getValue()));
            LocalDateTime start=startDate.getValue().atTime(Integer.parseInt(startHour.getValue()),Integer.parseInt(startMin.getValue()));
            var priceS=redPrice.getText();
            priceS=priceS.replace(',','.');
            double price=Double.valueOf(priceS);

            var priceRed=new StaticPriceReductionImpl(redName.getText(),end,start,isPercent.isSelected(),price);
            App.businessLogicAPI.createPriceReductionFromUI(priceRed);

        });
        setData();
        setTables();
    }
    public void setTables(){
        var allFlights = App.businessLogicAPI.getAllFlights(f -> true);
        System.out.println(allFlights);
        var flightTable = new FlightTable(allFlights, (event, row) -> {
            Flight selectedFlight = row.getItem();
            System.out.println("Clicked on: " + selectedFlight);
        });
        flightVBox.getChildren().add(flightTable);
        flightTable.setMinWidth(400);

        var allReductions = App.businessLogicAPI.getAllPriceReductions();
        System.out.println(allReductions);
        var reductionTable = new PriceReductionsTable(allReductions, (event, row) -> {
            PriceReduction selectedReduction = row.getItem();
            System.out.println("Clicked on: " + selectedReduction);
        });
        reductionsVBox.getChildren().add(1,reductionTable);
        reductionTable.setMinWidth(400);
    }
}
