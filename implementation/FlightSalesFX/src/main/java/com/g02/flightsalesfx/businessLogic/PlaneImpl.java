package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public PlaneImpl(String name, String type, String manufacturer, List<Seat> toAdd) {
        this(name, type, manufacturer);
        this.seatList = new ArrayList<>();
        seatList.addAll(toAdd);
    }


    @Override
    public String getName() {
        return this.name;
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
    public void addSeat(Seat s) {
        this.seatList.add(s);
    }

    @Override
    public void addAllSeats(List<? extends Seat> seatList) {
        this.seatList.addAll(seatList);
    }

    @Override
    public String toString() {
        return "Plane " + name + ", ID: " + type + ", Manufacturer: " + manufacturer + ", Seats: " + seatList.size() /*+ " Rows: " + (seatList.get(seatList.size() - 1).getRowNumber() + 1)*/;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaneImpl plane = (PlaneImpl) o;
        return Objects.equals(name, plane.name) && Objects.equals(manufacturer, plane.manufacturer) && Objects.equals(type, plane.type) && Objects.equals(seatList, plane.seatList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturer, type, seatList);
    }

    @Override
    public int getRows(){
        int rows = 0;
        if(seatList.size()!=0){
            rows = seatList.get(seatList.size() - 1).getRowNumber() + 1;
        }
        return rows;
    }
}
