package org.g02.flightsalesfx.businessLogic;

import javafx.application.Application;
import javafx.stage.Stage;
import org.g02.flightsalesfx.App;
import org.g02.flightsalesfx.TestUtil;
import org.g02.flightsalesfx.businessEntities.PriceReductionManager;
import org.g02.flightsalesfx.persistence.PersistenceAPI;
import org.g02.flightsalesfx.persistence.PersistenceAPIImpl;
import org.g02.flightsalesfx.persistence.PriceReductionStorageService;
import org.g02.flightsalesfx.persistence.PriceReductionStorageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(ApplicationExtension.class)
public class PriceReductionTest {

    @Test
    void testEquals() {
        StaticPriceReductionImpl ref=new StaticPriceReductionImpl("Winter Sale", LocalDateTime.of(2021,10,10,0,0),LocalDateTime.of(2020,10,10,0,0),false,50);
        StaticPriceReductionImpl eql=new StaticPriceReductionImpl("Winter Sale", LocalDateTime.of(2021,10,10,0,0),LocalDateTime.of(2020,10,10,0,0),false,50);
        StaticPriceReductionImpl une1=new StaticPriceReductionImpl("Winter Sale",LocalDateTime.of(2021,10,20,0,0),LocalDateTime.of(2020,10,20,0,0),false,10);
        StaticPriceReductionImpl une2=new StaticPriceReductionImpl("Winter Sale",LocalDateTime.of(2021,10,20,0,0),LocalDateTime.of(2020,10,20,0,0),true,50);
        StaticPriceReductionImpl une3=new StaticPriceReductionImpl("Winter Sale",LocalDateTime.of(2021,10,20,0,0),LocalDateTime.of(2019,10,20,0,0),false,50);
        StaticPriceReductionImpl une4=new StaticPriceReductionImpl("Winter Sale",LocalDateTime.of(2022,10,20,0,0),LocalDateTime.of(2020,10,20,0,0),false,50);
        StaticPriceReductionImpl une5=new StaticPriceReductionImpl("Summer Sale",LocalDateTime.of(2021,10,20,0,0),LocalDateTime.of(2020,10,20,0,0),false,50);
        TestUtil.verifyEqualsHasCode(ref, eql, une1, une2, une3, une4, une5);
    }

    @Test
    void testSave() throws IOException {
        var mockPR=Mockito.mock(PriceReductionStorageService.class);
        StaticPriceReductionImpl ref=new StaticPriceReductionImpl(
                "Winter Sale",
                LocalDateTime.of(2021,10,10,0,0),
                LocalDateTime.of(2020,10,10,0,0),
                false,
                50
        );
        PersistenceAPI pAPI=new PersistenceAPIImpl();
        BusinessLogicAPI bAPI=new BusinessLogicAPIImpl(pAPI);
        pAPI.setPriceReductionStorageService(mockPR);
        bAPI.createPriceReductionFromUI(ref);
        verify(mockPR).add(ref);
    }
}
