package org.g02.flightsalesfx.businessLogic;

import org.g02.btfdao.annotations.ForeignKey;
import org.g02.btfdao.annotations.PrimaryKey;
import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.businessEntities.SeatOption;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@TableName("seats")
public class SeatImpl implements Seat, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    public int rowNumber;
    public int seatNumber;
    @ForeignKey("com.g02.flightsalesfx.businessLogic.SeatOptionImpl")
    public SeatOptionImpl[] seatOptions;

    //    Useless constructor because cant be accessed from SeatManagerImpl
    public SeatImpl(int rowNumber, int seatNumber) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.seatOptions = new SeatOptionImpl[0];
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
        var collect = Arrays.stream(this.seatOptions).collect(Collectors.toList());
        if (!(so instanceof SeatOptionImpl)) return;
        collect.add((SeatOptionImpl) so);
        this.seatOptions = collect.toArray(new SeatOptionImpl[0]);
    }

    @Override
    public void addAllSeatOptions(List<? extends SeatOption> seatOptionList) {
        var collect = Arrays.stream(this.seatOptions).collect(Collectors.toList());
        collect.addAll((Collection<? extends SeatOptionImpl>) seatOptionList);
        this.seatOptions = collect.toArray(new SeatOptionImpl[0]);
    }

    @Override
    public List<SeatOption> getSeatOptions() {
        return Arrays.stream(seatOptions).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return "SeatImpl{" +
                "rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", seatOptions=" + Arrays.toString(seatOptions) +
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
        return id == seat.id && getRowNumber() == seat.getRowNumber() && getSeatNumber() == seat.getSeatNumber() && Arrays.equals(seatOptions, seat.seatOptions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, getRowNumber(), getSeatNumber());
        result = 31 * result + Arrays.hashCode(seatOptions);
        return result;
    }

    /*@Override
    public SeatOptionImpl[] getSeatOptions() {
        return seatOptions;
    }*/

    public static SeatImpl of(Seat s){
        return new SeatImpl(s.getRowNumber(), s.getSeatNumber());
    }
}
