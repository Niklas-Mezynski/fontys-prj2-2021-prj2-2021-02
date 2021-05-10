package org.g02.flightsalesfx;


import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class CreatePriceReductions implements Controller {

    @FXML
    public DatePicker startDate;
    public DatePicker endDate;
    public TextField redPrice;
    public TextField redName;
    public ComboBox startMin;
    public ComboBox endMin;
    public ComboBox endHour;
    public ComboBox startHour;
    public CheckBox isPercent;
    public Button addReduction;

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
        setData();
    }
}
