package org.g02.flightsalesfx;


import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreatePriceReductions implements Controller {

    private Flight flight;
    private PriceReduction reduction;
    private Button saveButton;

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
    public FlightTable flightTable;
    public Button exitButton;
    public HBox saveHBox;

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
        saveButton=new Button("Select to Save");
        saveButton.setDisable(true);
        saveButton.getStyleClass().add("buttonOrange");
        saveButton.setOnMouseClicked(e->{
            flight.addPriceReduction(reduction);
            App.persistenceAPI.getFlightStorageService(App.businessLogicAPI.getFlightManager());
        });
        saveHBox.getChildren().add(0,saveButton);
        addReduction.setOnMouseClicked((event)->{
            saveReductions();
        });
        setData();
        setTables();
    }

    public void saveReductions(){
        String eD=endDate.getEditor().getText();
        String sD=startDate.getEditor().getText();
        eD=eD.replace(',','.');
        eD=eD.replace('/','.');
        eD=eD.replace('\\','.');
        sD=sD.replace(',','.');
        sD=sD.replace('/','.');
        sD=sD.replace('\\','.');
        String[] eDs=eD.split("\\.");
        String[] sDs=sD.split("\\.");
        if(Integer.parseInt(eDs[2])<1000){
            eDs[2]="20"+eDs[2];
        }
        if(Integer.parseInt(sDs[2])<1000){
            sDs[2]="20"+sDs[2];
        }
        LocalDateTime end=LocalDateTime.of(Integer.parseInt(eDs[2]),Integer.parseInt(eDs[1]),Integer.parseInt(eDs[0]),Integer.parseInt(endHour.getValue()),Integer.parseInt(endMin.getValue()));
        LocalDateTime start=LocalDateTime.of(Integer.parseInt(sDs[2]),Integer.parseInt(sDs[1]),Integer.parseInt(sDs[0]),Integer.parseInt(startHour.getValue()),Integer.parseInt(startMin.getValue()));
        var priceS=redPrice.getText();
        priceS=priceS.replace(',','.');
        double price=Double.valueOf(priceS);

        var priceRed=new StaticPriceReductionImpl(redName.getText(),end,start,isPercent.isSelected(),price);
        App.businessLogicAPI.createPriceReductionFromUI(priceRed);
        updateTables();
    }

    public void updateTables(){
        reductionsVBox.getChildren().remove(1);
        var allReductions = App.businessLogicAPI.getAllPriceReductions();
        System.out.println(allReductions);
        var reductionTable = new PriceReductionsTable(allReductions, (event, row) -> {
            PriceReduction selectedReduction = row.getItem();
            System.out.println("Clicked on: " + selectedReduction);
        });
        reductionTable.setMinWidth(400);
        reductionsVBox.getChildren().add(1,reductionTable);
        reductionTable.setOnMouseClicked(e->{
            reduction=reductionTable.getSelectionModel().getSelectedItem();
            if(flight!=null){
                saveButton.setDisable(false);
                saveButton.setText("Add \""+reduction.getName()+"\" to Flight Nr. \""+flight.getFlightNumber()+"\"");
            }
        });
    }
    public void setTables(){
        var allFlights = App.businessLogicAPI.getAllFlights(f -> true);
        System.out.println(allFlights);
        flightTable = new FlightTable(allFlights, (event, row) -> {
            Flight selectedFlight = row.getItem();
            System.out.println("Clicked on: " + selectedFlight);
        });
        flightVBox.getChildren().add(1,flightTable);
        flightTable.setOnMouseClicked(e->{
            flight=flightTable.getSelectionModel().getSelectedItem();
            if(reduction!=null){
                saveButton.setDisable(false);
                saveButton.setText("Add \""+reduction.getName()+"\" to Flight Nr. \""+flight.getFlightNumber()+"\"");
            }
        });
        flightTable.setMinWidth(400);
        var allReductions = App.businessLogicAPI.getAllPriceReductions();
        System.out.println(allReductions);
        var reductionTable = new PriceReductionsTable(allReductions, (event, row) -> {
            PriceReduction selectedReduction = row.getItem();
            System.out.println("Clicked on: " + selectedReduction);
        });
        reductionTable.setMinWidth(400);
        reductionsVBox.getChildren().add(1,reductionTable);
        reductionTable.setOnMouseClicked(e->{
            reduction=reductionTable.getSelectionModel().getSelectedItem();
            if(flight!=null){
                saveButton.setDisable(false);
                saveButton.setText("Add \""+reduction.getName()+"\" to Flight Nr. \""+flight.getFlightNumber()+"\"");
            }
        });
    }

    public void exit() throws IOException {
        App.setRoot("home");
    }
}
