package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.TestUtil;
import org.junit.jupiter.api.Test;

public class SalesManagerTest {

    @Test
    void testEquals() {
        SalesManagerImpl ref = new SalesManagerImpl("Peter", "peter@gmx.de", "peter");
        SalesManagerImpl eql = new SalesManagerImpl("Peter", "peter@gmx.de", "peter");
        SalesManagerImpl uneql1 = new SalesManagerImpl("Frant", "peter@gmx.de", "peter");
        SalesManagerImpl uneql2 = new SalesManagerImpl("Peter", "franz@gmx.de", "peter");
        SalesManagerImpl uneql3 = new SalesManagerImpl("Peter", "peter@gmx.de", "franz");
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2, uneql3);
    }
}
