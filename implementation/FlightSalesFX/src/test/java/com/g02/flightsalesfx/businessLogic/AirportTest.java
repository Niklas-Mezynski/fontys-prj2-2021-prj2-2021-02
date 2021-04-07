package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.TestUtil;
import com.g02.flightsalesfx.businessEntities.Airport;
import org.junit.jupiter.api.Test;

public class AirportTest {

    @Test
    void test() {
        Airport a1 = new AirportImpl("DUS", "Düsseldorf", "Germany");
        Airport a2 = new AirportImpl("DUS", "Düsseldorf", "Germany");
        Airport a3 = new AirportImpl("DOS", "Düsseldorf", "Germany");
        Airport a4 = new AirportImpl("DUS", "Dosseldorf", "Germany");
        Airport a5 = new AirportImpl("DUS", "Düsseldorf", "Germani");
        TestUtil.verifyEqualsHasCode(a1, a2, a3, a4, a5);
    }

}
