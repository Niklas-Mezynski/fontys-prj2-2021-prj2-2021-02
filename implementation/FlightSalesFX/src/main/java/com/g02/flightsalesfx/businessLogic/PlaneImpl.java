package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Airport;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.persistence.AirportStorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlaneImpl implements Plane {

    private String name;
    private String manufacturer;
    private String type;
    private List<Seat> seatList;

    public PlaneImpl(String name, String type, String manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.type = type;
        this.seatList = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Seat> getAllSeats() {
        return seatList.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getSeatCount(){
        return seatList.size();
    }

    @Override
    public int getRowCount(){
        return seatList.stream().mapToInt(Seat::getRowNumber).max().orElse(-1) + 1;
    }

    @Override
    public void addSeat(Seat s) {
        this.seatList.add(s);
    }

    @Override
    public void addAllSeats(List<? extends Seat> seatList) {
        this.seatList.addAll(seatList);
    }

    @Override
    public String toString() {


        return "Plane " + name + ", ID: " + type + ", Manufacturer: " + manufacturer + ", Seats: " + seatList.size() + " Rows: " + getRows();
    }

    public int getRows(){
        int rows = 0;
        if(seatList.size()!=0){
            rows = seatList.get(seatList.size() - 1).getRowNumber() + 1;
        }
        return rows;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaneImpl plane = (PlaneImpl) o;
        return Objects.equals(getName(), plane.getName()) && Objects.equals(getManufacturer(), plane.getManufacturer()) && Objects.equals(getType(), plane.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturer, type);
    }
/*
    @Override
    public int getRows(){
        int rows = 0;
        if(seatList.size()!=0){
            rows = seatList.get(seatList.size() - 1).getRowNumber() + 1;
        }
        return rows;
    }*/

    @Override
    public List<Seat> getAllSeats() {
        return this.seatList;
    }
}
