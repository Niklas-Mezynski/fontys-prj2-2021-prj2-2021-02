package org.g02.flightsalesfx.businessLogic;

import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.g02.flightsalesfx.App;
import org.g02.flightsalesfx.TestUtil;
import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.persistence.FlightStorageService;
import org.g02.flightsalesfx.persistence.PersistenceAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ExtendWith(ApplicationExtension.class)
public class FlightTest {

    @Mock
    private PersistenceAPI persistenceAPI;
    @Mock
    private FlightStorageService flightStorageService;
    //SystemUnderTest
    private BusinessLogicAPIImpl businessLogicAPI;

    //sampleData
    private SalesOfficerImpl so = new SalesOfficerImpl("Officer", "officermail", "123");

    private Flight sampleFlight = new FlightImpl(so, 4,LocalDateTime.MIN, LocalDateTime.now(), new RouteImpl(new AirportImpl("DUS", "DÃ¼sseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl(2, "flieger", "Lufthansa", "A380"), 20);
    private List<Flight> flightList = List.of(sampleFlight);

    FlightTest() {
        //mocking persistence
        persistenceAPI = mock(PersistenceAPI.class);
        flightStorageService = mock(FlightStorageService.class);

        //instantiating SUT
        businessLogicAPI = new BusinessLogicAPIImpl(persistenceAPI);

        //train mock
        when(persistenceAPI.getFlightStorageService(any())).thenReturn(flightStorageService);
    }

//    @BeforeEach
//    void setSalesprocessToDefault() {
//        flightList.get(0).stopSalesProcess();
//    }

    @Test
    void testEquals() {
        Flight f1 = new FlightImpl(so, 4,LocalDateTime.MIN, LocalDateTime.now(), new RouteImpl(new AirportImpl("DUS", "DÃ¼sseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl(2, "flieger", "Lufthansa", "A380"), 20);
        Flight f2 = new FlightImpl(so, 4,LocalDateTime.MIN, LocalDateTime.now(), new RouteImpl(new AirportImpl("DUS", "DÃ¼sseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl(2, "flieger", "Lufthansa", "A380"), 20);
        Flight f3 = new FlightImpl(so, 5,LocalDateTime.MIN, LocalDateTime.now(), new RouteImpl(new AirportImpl("DUS", "DÃ¼sseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl(2, "flieger", "Lufthansa", "A380"), 20);
        Flight f4 = new FlightImpl(so, 5,LocalDateTime.MIN, LocalDateTime.now(), new RouteImpl(new AirportImpl("DUS", "DÃ¼sseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl(2, "flieger", "Lufthansa", "A380"), 70);
        Flight f5 = new FlightImpl(so, 5,LocalDateTime.MIN, LocalDateTime.now(), new RouteImpl(new AirportImpl("DUS", "DÃ¼sseldorf", "Germany"), new AirportImpl("BER", "Berlin", "Germany")), new PlaneImpl(7, "flieger", "Lufthansa", "A380"), 20);
        TestUtil.verifyEqualsHasCode(f1, f2, f3, f4, f5);
    }

    /**
     *
     * @param p boolean parameter => updateFlight(..., p)
     * @param expected expected boolean of getSalesprocessStatus()
     */
    @ParameterizedTest
    @CsvSource({
        // parameter, expected salesprocessStatus
            "true, true",
            "false, false",
    })
    void testUpdateChangesSalesprocessStatus01(String p, String expected) {
        var currentFlight = flightList.get(0);
        boolean boolParameter = Boolean.valueOf(p);
        boolean expectedBool = Boolean.valueOf(expected);

        //start Salesprocess:
        var returnedFlight = businessLogicAPI.updateFlight(FlightImpl.of(currentFlight), currentFlight.getDeparture(), currentFlight.getArrival(), currentFlight.getPrice(), boolParameter);
        ArgumentCaptor<Flight> flightCaptor = ArgumentCaptor.forClass(Flight.class);
        verify(persistenceAPI, times(2)).getFlightStorageService(any());
        verify(flightStorageService).update(flightCaptor.capture());

        Assertions.assertThat(flightCaptor.getValue().getSalesProcessStatus())
                .isEqualTo(expectedBool);
    }
}