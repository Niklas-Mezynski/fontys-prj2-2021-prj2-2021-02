package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class PlaneTest {

    @Test
    void testEquals() {
        PlaneImpl ref = new PlaneImpl("Peter", "peter@gmx.de", "peter");
        PlaneImpl eql = new PlaneImpl("Peter", "peter@gmx.de", "peter");
        PlaneImpl uneql1 = new PlaneImpl("Frant", "peter@gmx.de", "peter");
        PlaneImpl uneql2 = new PlaneImpl("Peter", "franz@gmx.de", "peter");
        PlaneImpl uneql3 = new PlaneImpl("Peter", "peter@gmx.de", "franz");
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2, uneql3);
    }
}
