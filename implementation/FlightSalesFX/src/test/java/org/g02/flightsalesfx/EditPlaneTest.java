package org.g02.flightsalesfx;

import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.assertj.core.api.Assumptions;
import org.assertj.core.api.SoftAssertions;
import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import org.g02.flightsalesfx.businessLogic.PlaneImpl;
import org.g02.flightsalesfx.businessLogic.SeatImpl;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.g02.flightsalesfx.businessLogic.SeatOptionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.NodeQuery;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
@ExtendWith(ApplicationExtension.class)
public class EditPlaneTest {

    private Stage stage;

    @Mock
    private BusinessLogicAPI businessLogicAPI;

    private SeatOptionImpl seatOptionFirstClass = new SeatOptionImpl("First Class", 40D);
    private SeatOptionImpl seatOptionBClass = new SeatOptionImpl("Business Class", 20D);
    private SeatOptionImpl seatOptionLegRoom = new SeatOptionImpl("Extra Leg Room", 10D);
    private List<SeatOptionImpl> allSeatOptions = List.of(seatOptionFirstClass, seatOptionBClass, seatOptionLegRoom);

    private Plane plane1 = new PlaneImpl("D-ABCD", "A", "A");
    private Plane plane2 = new PlaneImpl("D-BCDE", "B", "B");
    private List<Seat> seats = List.of(
            new SeatImpl(0, 0, List.of(seatOptionFirstClass, seatOptionLegRoom)),
            new SeatImpl(1, 0, List.of(seatOptionBClass)),
            new SeatImpl(1, 1, List.of(seatOptionBClass, seatOptionBClass))
    );
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
        businessLogicAPI = mock(BusinessLogicAPI.class);
        App.businessLogicAPI = businessLogicAPI;
        when(businessLogicAPI.getAllPlanes(any())).thenReturn(planes);
        App.setRoot("home");
        this.stage = stage;
    }

    @BeforeEach
    void setUp(FxRobot fxRobot) {
        verify(businessLogicAPI).getAllPlanes(any());
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
        Assertions.assertThat(lookup).hasSize(seats.size());
    }

    @Test
    void t02testPlaneSeatsLoaded(FxRobot fxRobot) {
        var lookup = fxRobot.lookup((Node node) -> node instanceof CreatePlaneController.SeatButton)
                .queryAllAs(CreatePlaneController.SeatButton.class);
        System.out.println(lookup);
        var seats = lookup.stream().map(seatButton -> new SeatImpl(seatButton.row(), seatButton.column())).collect(Collectors.toList());
        Assertions.assertThat(seats).hasSize(this.seats.size());
        var maxRow = lookup.stream().max(Comparator.comparingInt(CreatePlaneController.SeatButton::row));
        assumeThat(maxRow).isPresent();
        maxRow.ifPresent(seatButton -> assertThat(seatButton.row()).isEqualTo(1));
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(Seat.getSeatsInRow(seats, 0)).hasSize(1);
            softAssertions.assertThat(Seat.getSeatsInRow(seats, 1)).hasSize(2);
        });
    }

    @Test
    void t03testDeletePlane(FxRobot fxRobot) {
        var lookup = fxRobot.lookup("#deleteButton").queryButton();
        when(businessLogicAPI.deletePlane(any())).thenReturn(true);
        fxRobot.clickOn(lookup);
        verify(businessLogicAPI).deletePlane(any());
    }

    @Test
    void t04testUpdatePlane(FxRobot fxRobot) {
        var lookup = fxRobot.lookup("#savePlaneButton").queryButton();
        when(businessLogicAPI.updatePlane(any(), any(), any(), any(), any())).thenReturn(plane1);
        fxRobot.clickOn(lookup);
        verify(businessLogicAPI).updatePlane(any(), any(), any(), any(), any());
    }

    @Test
    void t05testSeatOptionLoaded(FxRobot fxRobot) {
        var lookup = fxRobot.lookup("#seatOptions").queryAs(VBox.class);
        var children = lookup.getChildren();
        assertThat(children).hasSizeGreaterThan(2);
        var nodes = children.subList(1, children.size() - 2);
        assertThat(nodes).hasSize(allSeatOptions.size());
        List<SeatOptionImpl> seatOptions = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof HBox) {
                TextField textField = (TextField) ((HBox) node).getChildren().get(1);
                Spinner<Double> doubleSpinner = (Spinner<Double>) ((HBox) node).getChildren().get(2);
                var seatOption = new SeatOptionImpl(textField.getText(), doubleSpinner.getValue());
                seatOptions.add(seatOption);
            }
        }
        assertThat(allSeatOptions).containsAll(seatOptions);
    }
}
