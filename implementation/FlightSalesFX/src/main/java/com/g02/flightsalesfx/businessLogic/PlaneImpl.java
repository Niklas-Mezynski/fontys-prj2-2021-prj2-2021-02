package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;
import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Seat;

import java.util.*;
import java.util.stream.Collectors;

@TableName("planes")
public class PlaneImpl implements Plane, Savable {

    @PrimaryKey
    public String name;
    public String manufacturer;
    public String type;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatImpl")
    public SeatImpl[] seatList;

    public PlaneImpl(String name, String type, String manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.type = type;
        this.seatList = new SeatImpl[0];
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
    public int getSeatCount(){
        return seatList.length;
    }

    @Override
    public int getRowCount(){
        return Arrays.stream(seatList).mapToInt(Seat::getRowNumber).max().orElse(-1) + 1;
    }

    @Override
    public void addSeat(Seat s) {
        var collect = Arrays.stream(this.seatList).collect(Collectors.toList());
        if (!(s instanceof SeatImpl)) return;
        collect.add((SeatImpl) s);
        this.seatList = collect.toArray(new SeatImpl[0]);
    }

    @Override
    public void addAllSeats(List<? extends Seat> seatList) {
        var collect = Arrays.stream(this.seatList).collect(Collectors.toList());
        collect.addAll((Collection<? extends SeatImpl>) seatList);
        this.seatList = collect.toArray(new SeatImpl[0]);
    }

    @Override
    public String toString() {


        return "Plane " + name + ", ID: " + type + ", Manufacturer: " + manufacturer + ", Seats: " + seatList.length + " Rows: " + getRows();
    }

    public int getRows(){
        int rows = 0;
        if(seatList.length!=0){
            rows = seatList[seatList.length - 1].getRowNumber() + 1;
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
        if(seatList.length!=0){
            rows = seatList.get(seatList.length - 1).getRowNumber() + 1;
        }
        return rows;
    }*/
}
