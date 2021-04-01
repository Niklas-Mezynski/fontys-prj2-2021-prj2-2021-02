package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class SeatOptionTest {

    @Test
    void testEquals() {
        SeatOptionImpl ref = new SeatOptionImpl("Peter");
        SeatOptionImpl eql = new SeatOptionImpl("Peter");
        SeatOptionImpl uneql1 = new SeatOptionImpl("Frant");
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1);
    }
}
