package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class SeatOptionTest {

    @Test
    void testEquals() {
        SeatOptionImpl ref = new SeatOptionImpl("Peter", 100.0);
        SeatOptionImpl eql = new SeatOptionImpl("Peter", 100.0);
        SeatOptionImpl uneql1 = new SeatOptionImpl("Franz", 100.0);
        SeatOptionImpl uneql2 = new SeatOptionImpl("Peter", 99.0);
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2);
    }
}
