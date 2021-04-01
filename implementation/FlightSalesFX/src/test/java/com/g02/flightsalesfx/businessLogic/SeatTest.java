package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class SeatTest {

    @Test
    void testEquals() {
        SeatImpl ref = new SeatImpl(1, 1);
        SeatImpl eql = new SeatImpl(1, 1);
        SeatImpl uneql1 = new SeatImpl(1,0);
        SeatImpl uneql2 = new SeatImpl(0,1);
        SeatImpl uneql3 = new SeatImpl(0,0);
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2, uneql3);
    }
}
