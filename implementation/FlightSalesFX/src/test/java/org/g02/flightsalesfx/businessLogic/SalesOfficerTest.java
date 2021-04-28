package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class SalesOfficerTest {

    @Test
    void testEquals() {
        SalesOfficerImpl ref = new SalesOfficerImpl("Peter", "peter@gmx.de", "peter");
        SalesOfficerImpl eql = new SalesOfficerImpl("Peter", "peter@gmx.de", "peter");
        SalesOfficerImpl uneql1 = new SalesOfficerImpl("Frant", "peter@gmx.de", "peter");
        SalesOfficerImpl uneql2 = new SalesOfficerImpl("Peter", "franz@gmx.de", "peter");
        SalesOfficerImpl uneql3 = new SalesOfficerImpl("Peter", "peter@gmx.de", "franz");
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2, uneql3);
    }
}
