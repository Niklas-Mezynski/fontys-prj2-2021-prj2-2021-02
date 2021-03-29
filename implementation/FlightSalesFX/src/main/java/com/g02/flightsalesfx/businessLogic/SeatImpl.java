package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.businessEntities.SeatOption;
import java.util.List;
import java.util.ArrayList;

public class SeatImpl implements Seat, Comparable<Seat> {

    private int rowNumber;
    private int seatNumber;
    private List<SeatOption> seatOptions;

    public SeatImpl(int rowNumber, int seatNumber) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.seatOptions = new ArrayList<>();
    }

    public SeatImpl(int rowNumber, int seatNumber, List<SeatOption> toAdd) {
        this(rowNumber, seatNumber);
        this.seatOptions.addAll(toAdd);
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
        this.seatOptions.add(so);
    }

    @Override
    public void addAllSeatOptions(List<? extends SeatOption> seatOptionList) {
        this.seatOptions.addAll(seatOptionList);
    }

    @Override
    public String toString() {
        return "SeatImpl{" +
                "rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", seatOptions=" + seatOptions +
                '}';
    }

    @Override
    public int compareTo(Seat o) {
        if(this.getRowNumber() == o.getRowNumber()){
            return this.getSeatNumber()-o.getSeatNumber();
        }
        return this.getRowNumber()-o.getRowNumber();
    }
}
