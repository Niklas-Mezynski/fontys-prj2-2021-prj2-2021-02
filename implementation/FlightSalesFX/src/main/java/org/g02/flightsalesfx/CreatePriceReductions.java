package org.g02.flightsalesfx;


import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.businessEntities.PriceReduction;
import org.g02.flightsalesfx.businessLogic.FlightImpl;
import org.g02.flightsalesfx.businessLogic.StaticPriceReductionImpl;
import org.g02.flightsalesfx.gui.FlightTable;
import org.g02.flightsalesfx.gui.PriceReductionsTable;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
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
    public PriceReductionsTable reductionTable;
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
            if(!flight.getPriceReductions().contains(reduction)){
                flight.addPriceReduction(reduction);
                App.persistenceAPI.getFlightStorageService(App.businessLogicAPI.getFlightManager()).update(flight);
                saveButton.setText("Successfully Saved");
                saveButton.setDisable(true);
            }
            else {
                saveButton.setDisable(true);
                saveButton.setText("Already Saved");
            }
        });
        saveHBox.getChildren().add(0,saveButton);
        setData();
        helperSetTables();
    }
    public void helperSaveReductions(){
        LocalDateTime end=null;
        LocalDateTime start=null;
        boolean biggerThan=false;
        double price=-5;
        try {
            String eD = endDate.getEditor().getText();
            String sD = startDate.getEditor().getText();
            eD = eD.replace(',', '.');
            eD = eD.replace('/', '.');
            eD = eD.replace('\\', '.');
            String[] eDs = eD.split("\\.");
            if (Integer.parseInt(eDs[2]) < 1000) {
                eDs[2] = "20" + eDs[2];
            }
            end = LocalDateTime.of(Integer.parseInt(eDs[2]), Integer.parseInt(eDs[1]), Integer.parseInt(eDs[0]), Integer.parseInt(endHour.getValue()), Integer.parseInt(endMin.getValue()));
            sD = sD.replace(',', '.');
            sD = sD.replace('/', '.');
            sD = sD.replace('\\', '.');
            String[] sDs = sD.split("\\.");
            if (Integer.parseInt(sDs[2]) < 1000) {
                sDs[2] = "20" + sDs[2];
            }
            start = LocalDateTime.of(Integer.parseInt(sDs[2]), Integer.parseInt(sDs[1]), Integer.parseInt(sDs[0]), Integer.parseInt(startHour.getValue()), Integer.parseInt(startMin.getValue()));
            biggerThan=start.isBefore(end);
            var priceS=redPrice.getText();
            priceS=priceS.replace(',','.');
            price=Double.valueOf(priceS);

            var priceRed=new StaticPriceReductionImpl(redName.getText().trim(),end,start,isPercent.isSelected(),price);
            boolean saved=true;
            if(biggerThan) {
                if (App.businessLogicAPI.createPriceReductionFromUI(priceRed)) {
                    helperUpdateTables();
                    resetFields();
                }
                else{
                    saved=false;
                }
            }
            else{
                saved=false;
            }
            if(!saved){
                System.out.println("Failed to Save "+priceRed);
                throw new Exception("Failed to Save");
            }
        }
        catch (Exception disable){Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving Price Reduction");
            String contentText="";
            if(start==null){
                contentText="Failed to save start Date. Use DD.MM.YYYY as format. Use \'.\' or \',\' or \'\\\' or \'/\' to split the Dates. Try again!";
            }
            else if(end==null){
                contentText="Failed to save end Date. Use DD.MM.YYYY as format. Use \'.\' or \',\' or \'\\\' or \'/\' to split the Dates. Try again!";
            }
            else if(!biggerThan){
                contentText= "Start Date is bigger than End Date. Try again!";
            }
            else if(price==-5){
                contentText="Failed to save Price Reduction. Use \'.\' or \',\' to use Decimals. Try again!";
            }
            else if(reduction.getEndTime().isBefore(end)){
                contentText="Time is not inside the Flight Time. Try again!";
            }
            else {
                contentText = "There was an error while saving the created Price Reduction. Try again!";
            }
            alert.setContentText(contentText);
            alert.showAndWait();
        }
    }
    private void resetFields() {
        endDate.getEditor().setText(null);
        endMin.setValue(null);
        endHour.setValue(null);
        startDate.getEditor().setText(null);
        startMin.setValue(null);
        startHour.setValue(null);
        redPrice.setText("");
        redName.setText("");
        isPercent.setSelected(false);

    }
    public void helperUpdateTables(){
        reductionsVBox.getChildren().remove(1);
        var allReductions = App.businessLogicAPI.getAllPriceReductions();
        System.out.println(allReductions);
        reductionTable = new PriceReductionsTable(allReductions, (event, row) -> {
            PriceReduction selectedReduction = row.getItem();
            System.out.println("Clicked on: " + selectedReduction);
        });
        reductionTable.setMinWidth(400);
        reductionsVBox.getChildren().add(1,reductionTable);
        reductionTable.setOnMouseClicked(e->{
            reduction=reductionTable.getSelectionModel().getSelectedItem();
            if(flight!=null){
                if(!flight.getPriceReductions().contains(reduction)) {
                    saveButton.setDisable(false);
                    saveButton.setText("Add \"" + reduction.getName() + "\" to Flight Nr. \"" + flight.getFlightNumber() + "\"");
                }
                else{
                    saveButton.setDisable(true);
                    saveButton.setText("Already Saved");
                }
            }
        });
    }
    public void helperSetTables(){
        var allFlights = App.businessLogicAPI.getAllFlights(f -> true);
        flightTable = new FlightTable(allFlights, (event, row) -> {
            Flight selectedFlight = row.getItem();
            System.out.println("Clicked on: " + selectedFlight);
        });
        flightVBox.getChildren().add(1,flightTable);
        flightTable.setOnMouseClicked(e->{
            flight=flightTable.getSelectionModel().getSelectedItem();
            if(reduction!=null){
                if(flight!=null) {
                    if(!flight.getPriceReductions().contains(reduction)) {
                        saveButton.setDisable(false);
                        saveButton.setText("Add \"" + reduction.getName() + "\" to Flight Nr. \"" + flight.getFlightNumber() + "\"");
                    }
                    else{
                        saveButton.setDisable(true);
                        saveButton.setText("Already Saved");
                    }
                }
            }
        });
        flightTable.setMinWidth(400);
        var allReductions = App.businessLogicAPI.getAllPriceReductions();
        reductionTable = new PriceReductionsTable(allReductions, (event, row) -> {
            PriceReduction selectedReduction = row.getItem();
            System.out.println("Clicked on: " + selectedReduction);
        });
        reductionTable.setMinWidth(400);
        reductionsVBox.getChildren().add(1,reductionTable);
        reductionTable.setOnMouseClicked(e->{
            reduction=reductionTable.getSelectionModel().getSelectedItem();
            if(flight!=null){
                if(reduction!=null) {
                    if(!flight.getPriceReductions().contains(reduction)) {
                        saveButton.setDisable(false);
                        saveButton.setText("Add \"" + reduction.getName() + "\" to Flight Nr. \"" + flight.getFlightNumber() + "\"");
                    }
                    else{
                        saveButton.setDisable(true);
                        saveButton.setText("Already Saved");
                    }
                }
            }
        });
    }
    public void exit() throws IOException {
        App.setRoot("home");
    }
}
