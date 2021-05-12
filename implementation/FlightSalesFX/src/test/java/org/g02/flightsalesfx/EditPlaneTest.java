package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import org.g02.flightsalesfx.businessLogic.PlaneImpl;
import org.g02.flightsalesfx.businessLogic.SeatImpl;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(ApplicationExtension.class)
public class EditPlaneTest {

    private Stage stage;

    @Mock
    private BusinessLogicAPI businessLogicAPI;


    private Plane plane1 = new PlaneImpl("D-ABCD", "A", "A");
    private Plane plane2 = new PlaneImpl("D-BCDE", "B", "B");
    private List<Seat> seats = List.of(new SeatImpl(0, 0), new SeatImpl(1, 0));
    private List<Plane> planes = List.of(plane1);

    /**
     * Setting up the test.
     * Create the mocks and inject it into the App
     * Skip Login and move to the home page
     *
     * @param stage
     * @throws IOException if home.fxml can't be found
     */
    @Start
    void start(Stage stage) throws IOException {
        plane1.addAllSeats(seats);
        var app = new App();
        app.start(stage);
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        App.businessLogicAPI = businessLogicAPI;
        Mockito.when(businessLogicAPI.getAllPlanes(any())).thenReturn(planes);
        App.setRoot("home");
        this.stage = stage;
    }

    @BeforeEach
    void setUp(FxRobot fxRobot) {
        Mockito.verify(businessLogicAPI).getAllPlanes(any());
        fxRobot.rootNode(fxRobot.lookup("#planeTable").query());
        var lookup = fxRobot.lookup((Node node) -> node instanceof TableRow).queryAs(TableRow.class);
        fxRobot.doubleClickOn(lookup);
        System.out.println(lookup);
    }

    @Test
    void t01testPlaneLoaded(FxRobot fxRobot) {
        var lookup = fxRobot.lookup((Node node) -> node instanceof CreatePlaneController.SeatButton)
                .queryAllAs(CreatePlaneController.SeatButton.class);
        System.out.println(lookup);
        Assertions.assertThat(lookup).hasSize(1);
    }

    @Test
    void t02testPlaneSeatsLoaded() {

    }
}
