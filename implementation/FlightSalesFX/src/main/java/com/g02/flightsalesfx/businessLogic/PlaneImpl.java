package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Seat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlaneImpl implements Plane {

    private String name;
    private String manufacturer;
    private String type;
    private List<Seat> seatList;

    public PlaneImpl(String name, String manu, String type) {
        this.name = name;
        this.manufacturer = manu;
        this.type = type;
        this.seatList = new ArrayList<>();
    }

    public PlaneImpl(String name, String manu, String type, List<Seat> toAdd) {
        this.name = name;
        this.manufacturer = manu;
        this.type = type;
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
        return "PlaneImpl{" +
                "name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", type='" + type + '\'' +
                ", seatList=" + seatList +
                '}';
    }
}