package org.g02.flightsalesfx.businessLogic;

import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.g02.flightsalesfx.App;
import org.g02.flightsalesfx.TestUtil;
import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.persistence.PersistenceAPI;
import org.g02.flightsalesfx.persistence.PlaneStorageService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ExtendWith(ApplicationExtension.class)
public class PlaneTest {

    @Mock
    private PersistenceAPI persistenceAPI;
    @Mock
    private PlaneStorageService planeStorageService;
    private BusinessLogicAPIImpl businessLogicAPI;
    private SeatOptionImpl seatOptionFirstClass = new SeatOptionImpl("First Class", 40D);
    private SeatOptionImpl seatOptionBClass = new SeatOptionImpl("Business Class", 20D);
    private SeatOptionImpl seatOptionLegRoom = new SeatOptionImpl("Extra Leg Room", 10D);
    private final PlaneImpl plane1 = new PlaneImpl(1, "D-ABCD", "A", "A");
    private final List<Seat> seats = List.of(
            new SeatImpl(0, 0, List.of(seatOptionFirstClass, seatOptionLegRoom)),
            new SeatImpl(1, 0, List.of(seatOptionBClass)),
            new SeatImpl(1, 1, List.of(seatOptionBClass, seatOptionBClass))
    );
    private final Plane plane2 = new PlaneImpl(2, "D-BCDE", "B", "B").addAllSeats(seats);
    private List<SeatOptionImpl> allSeatOptions = List.of(seatOptionFirstClass, seatOptionBClass, seatOptionLegRoom);
    private List<Plane> planes = List.of(plane1);

    private Map<String, Plane> planeHashMap = Map.of("1", plane1, "2", plane2);
    private Map<String, List<Seat>> seatMap = Map.of("1", seats);

    @Start
    void start(Stage stage) throws IOException {
        App app = new App();
        app.start(stage);
        persistenceAPI = mock(PersistenceAPI.class);
        planeStorageService = mock(PlaneStorageService.class);
        businessLogicAPI = new BusinessLogicAPIImpl(persistenceAPI);
        when(persistenceAPI.getPlaneStorageService(any())).thenReturn(planeStorageService);
    }

    @Test
    void testEquals() {
        PlaneImpl ref = new PlaneImpl("Peter", "peter@gmx.de", "peter");
        PlaneImpl eql = new PlaneImpl("Peter", "peter@gmx.de", "peter");
        PlaneImpl uneql1 = new PlaneImpl("Frant", "peter@gmx.de", "peter");
        PlaneImpl uneql2 = new PlaneImpl("Peter", "franz@gmx.de", "peter");
        PlaneImpl uneql3 = new PlaneImpl("Peter", "peter@gmx.de", "franz");
        TestUtil.verifyEqualsHasCode(ref, eql, uneql1, uneql2, uneql3);
    }

    @ParameterizedTest
    @CsvSource({
            "1,2,D-BCDE,B,B,1",
    })
    void t01testUpdateProducesCorrectPlanes(String keyOld, String keyNew, String newName, String newMan, String newType, String seatKey) {
        var planeOld = planeHashMap.get(keyOld);
        var planeNew = planeHashMap.get(keyNew);
        var seats = seatMap.get(seatKey);
        var planeProduced = businessLogicAPI.updatePlane(PlaneImpl.of(planeOld), newName, newMan, newType, seats);
        ArgumentCaptor<Plane> planeArgumentCaptor = ArgumentCaptor.forClass(Plane.class);
        verify(persistenceAPI, times(2)).getPlaneStorageService(any());
        verify(planeStorageService).update(planeArgumentCaptor.capture());
        Assertions.assertThat(planeArgumentCaptor.getValue()).isEqualTo(planeNew);
    }

    @ParameterizedTest
    @CsvSource({
            "1"
    })
    void t02testDeleteCallsCorrectMethod(String key) {
        PlaneImpl plane = (PlaneImpl) planeHashMap.get(key);
        businessLogicAPI.deletePlane(plane);
        verify(planeStorageService).delete(any());
    }
}
