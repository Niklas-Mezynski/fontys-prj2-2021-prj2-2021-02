package com.g02.flightsalesfx.businessLogic;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.utils.Savable;
import com.g02.flightsalesfx.businessEntities.SeatOption;

import java.util.Objects;

@TableName("seatoptions")
public class SeatOptionImpl implements SeatOption, Savable {

    @PrimaryKey(autogen = true)
    public int id;
    public String name;

    public SeatOptionImpl (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "SeatOptionImpl{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatOptionImpl that = (SeatOptionImpl) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
