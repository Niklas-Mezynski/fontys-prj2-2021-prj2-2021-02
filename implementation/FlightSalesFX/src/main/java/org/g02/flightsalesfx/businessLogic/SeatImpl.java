package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.ForeignKey;
import org.g02.btfdao.annotations.Ignore;
import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.businessEntities.SeatOption;

import java.util.*;
import java.util.stream.Collectors;

@TableName("seats")
public class SeatImpl implements Seat, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    public int rowNumber;
    public int seatNumber;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatOptionImpl")
    private SeatOptionImpl[] seatOptionsDB = new SeatOptionImpl[0];
    @Ignore
    private List<SeatOption> seatOptions;

    //    Useless constructor because cant be accessed from SeatManagerImpl
    public SeatImpl(int rowNumber, int seatNumber) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.seatOptions = new ArrayList<>();
    }

    private SeatImpl() {
    }

    public SeatImpl(int rowNumber, int seatNumber, List<SeatOption> toAdd) {
        this(rowNumber, seatNumber);
        addAllSeatOptions(toAdd);
    }

    @Override
    public int getRowNumber() {
        return this.rowNumber;
    }

    @Override
    public int getSeatNumber() {
        return this.seatNumber;
    }

    @Override
    public void addSeatOption(SeatOption so) {
        seatOptions.add(so);
    }

    @Override
    public void addAllSeatOptions(List<? extends SeatOption> seatOptionList) {
        seatOptions.addAll(seatOptionList);
    }

    @Override
    public List<SeatOption> getSeatOptions() {
        return seatOptions;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return "SeatImpl{" +
                "rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", seatOptions=" + seatOptions +
                ", seatOptionsDB" + Arrays.toString(seatOptionsDB) +
                '}';
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatImpl seat = (SeatImpl) o;
        return rowNumber == seat.rowNumber && seatNumber == seat.seatNumber && Arrays.equals(seatOptions, seat.seatOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, seatNumber, seatOptions);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatImpl seat = (SeatImpl) o;
        return id == seat.id && getRowNumber() == seat.getRowNumber() && getSeatNumber() == seat.getSeatNumber() && seatOptions.equals(seat.seatOptions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, getRowNumber(), getSeatNumber());
        result = 31 * result + seatOptions.hashCode();
        return result;
    }

    /*@Override
    public SeatOptionImpl[] getSeatOptions() {
        return seatOptions;
    }*/

    public static SeatImpl of(Seat s) {
        var seat = new SeatImpl(s.getRowNumber(), s.getSeatNumber());
        seat.id=s.getID();
        seat.addAllSeatOptions(s.getSeatOptions());
        return seat;
    }

    private void beforeDeconstruction() {
        seatOptionsDB = seatOptions.stream()
                .map(SeatOptionImpl::of)
                .toArray(SeatOptionImpl[]::new);
    }

    private void afterConstruction() {
        seatOptions = Arrays.stream(seatOptionsDB).collect(Collectors.toList());
    }
}
