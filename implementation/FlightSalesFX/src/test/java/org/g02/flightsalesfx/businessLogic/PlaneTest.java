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

import static org.assertj.core.api.Assertions.assertThat;
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
    private final Plane plane2 = new PlaneImpl(1, "D-BCDE", "B", "B").addAllSeats(seats);
    private final Plane plane3 = new PlaneImpl(1, "D-CDEF", "C", "B");
    private final Plane plane4 = new PlaneImpl(1, "D-ABCD", "A", "A").addAllSeats(seats);
    private List<SeatOptionImpl> allSeatOptions = List.of(seatOptionFirstClass, seatOptionBClass, seatOptionLegRoom);
    private List<Plane> planes = List.of(plane1);

    private Map<String, Plane> planeHashMap = Map.of("1", plane1, "2", plane2, "3", plane3, "4", plane4);
    private Map<String, List<Seat>> seatMap = Map.of("0", List.of(), "1", seats);

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

    /**
     * Test that the business logic updates the plane correctly and the correct object is passed to the persistence layer
     * @param keyOld
     * @param keyNew
     * @param newName
     * @param newMan
     * @param newType
     * @param seatKey
     */
    @ParameterizedTest
    @CsvSource({
            "1,2,D-BCDE,B,B,1",
            "1,1,D-ABCD,A,A,0",
            "1,3,D-CDEF,C,B,0",

    })
    void t01testUpdateProducesCorrectPlanes(String keyOld, String keyNew, String newName, String newMan, String newType, String seatKey) {
        var planeOld = planeHashMap.get(keyOld);
        var planeNew = planeHashMap.get(keyNew);
        var seats = seatMap.get(seatKey);
        var planeProduced = businessLogicAPI.updatePlane(PlaneImpl.of(planeOld), newName, newMan, newType, seats);
        ArgumentCaptor<Plane> planeArgumentCaptor = ArgumentCaptor.forClass(Plane.class);
        verify(persistenceAPI, times(2)).getPlaneStorageService(any());
        verify(planeStorageService).update(planeArgumentCaptor.capture());
        assertThat(planeArgumentCaptor.getValue()).isEqualTo(planeNew);
        assertThat(planeArgumentCaptor.getValue().getId()).isEqualTo(planeOld.getId());
        assertThat(planeArgumentCaptor.getValue().getId()).isEqualTo(planeNew.getId());
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

    @ParameterizedTest
    @CsvSource({
            "1,4,1,true",
            "1,4,1,false"
    })
    void t03addSeatWorking(String plane, String expectedPlane, String seats, boolean individual) {
        PlaneImpl planeOld = (PlaneImpl) planeHashMap.get(plane);
        PlaneImpl planeExpected = (PlaneImpl) planeHashMap.get(expectedPlane);
        var seatsList = seatMap.get(seats);
        if (individual) {
            seatsList.forEach(planeOld::addSeat);
        } else {
            planeOld.addAllSeats(seatsList);
        }
        assertThat(planeOld).isEqualTo(planeExpected);
        assertThat(planeOld.getAllSeats()).containsAll(seatsList);
    }

    @ParameterizedTest
    @CsvSource({
            "D-ABCD,A,A,0,1",
            "D-BCDE,B,B,1,2",
    })
    void t04testCreatePlaneMethodWorking(String name, String man, String type, String seatsKey, String planeKey) {
        businessLogicAPI.createPlaneFromUI(name, man, type, seatMap.get(seatsKey));
        var plane = planeHashMap.get(planeKey);
        ArgumentCaptor<Plane> planeArgumentCaptor = ArgumentCaptor.forClass(Plane.class);
        verify(planeStorageService).add(planeArgumentCaptor.capture());
        assertThat(planeArgumentCaptor.getValue()).isEqualTo(plane);
    }
}
