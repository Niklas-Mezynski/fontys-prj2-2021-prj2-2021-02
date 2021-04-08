package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class RouteTest {

    @Test
    void testEquals() {
        RouteImpl ref = new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("DoS", "Düsseldorf", "Germany"));
        RouteImpl eql = new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Germany"), new AirportImpl("DoS", "Düsseldorf", "Germany"));
        RouteImpl uneql1 = new RouteImpl(new AirportImpl("BOR", "Düsseldorf", "Germany"), new AirportImpl("DoS", "Düsseldorf", "Germany"));
        RouteImpl uneql2 = new RouteImpl(new AirportImpl("DUS", "Dortmund", "Germany"), new AirportImpl("DoS", "Düsseldorf", "Germany"));
        RouteImpl uneql3 = new RouteImpl(new AirportImpl("DUS", "Düsseldorf", "Litauen"), new AirportImpl("FAN", "Düsseldorf", "Germany"));
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2, uneql3);
    }
}
