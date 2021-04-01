package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class SalesEmployeeTest {

    @Test
    void testEquals() {
        SalesEmployeeImpl ref = new SalesEmployeeImpl("Peter", "peter@gmx.de", "peter");
        SalesEmployeeImpl eql = new SalesEmployeeImpl("Peter", "peter@gmx.de", "peter");
        SalesEmployeeImpl uneql1 = new SalesEmployeeImpl("Frant", "peter@gmx.de", "peter");
        SalesEmployeeImpl uneql2 = new SalesEmployeeImpl("Peter", "franz@gmx.de", "peter");
        SalesEmployeeImpl uneql3 = new SalesEmployeeImpl("Peter", "peter@gmx.de", "franz");
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2, uneql3);
    }
}
