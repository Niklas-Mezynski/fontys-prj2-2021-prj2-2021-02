package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.ForeignKey;
import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Seat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@TableName("planes")
public class PlaneImpl implements Plane, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    public String name;
    public String manufacturer;
    public String type;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatImpl")
    public SeatImpl[] seatList = new SeatImpl[0];

    public PlaneImpl(int id, String name, String manufacturer, String type) {
        this(name, manufacturer, type);
        this.id = id;
    }

    public PlaneImpl(String name, String type, String manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.type = type;
        this.seatList = new SeatImpl[0];
    }

    private PlaneImpl() {
    }

    public static PlaneImpl of(Plane plane) {
        var plane1 = plane == null ? null : new PlaneImpl(plane.getId(), plane.getName(), plane.getType(), plane.getManufacturer());
        if (plane != null) {
            plane1.addAllSeats(plane.getAllSeats());
        }
        return plane1;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Seat> getAllSeats() {
        return Arrays.stream(seatList).collect(Collectors.toUnmodifiableList());
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
    public int getSeatCount() {
        return seatList.length;
    }

    @Override
    public int getRowCount() {
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
    public PlaneImpl addAllSeats(List<? extends Seat> seatList) {
        var collect = Arrays.stream(this.seatList).collect(Collectors.toList());
        collect.addAll((Collection<? extends SeatImpl>) seatList);
        System.out.println(Arrays.deepToString(collect.toArray()));
        this.seatList = collect.toArray(new SeatImpl[0]);
        return this;
    }

    @Override
    public String toString() {
        return "Plane " + name + ", ID: " + id + ", Manufacturer: " + manufacturer + ", Seats: " + seatList.length + " Rows: " + getRows();
    }

    public int getRows() {
        int rows = 0;
        if (seatList.length != 0) {
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
/*
    @Override
    public int getRows(){
        int rows = 0;
        if(seatList.length!=0){
            rows = seatList.get(seatList.length - 1).getRowNumber() + 1;
        }
        return rows;
    }*/

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturer, type);
    }

}
