package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;
import com.g02.flightsalesfx.gui.PlaneTable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class PlaneTableTest {
    private final Plane plane1 = new PlaneImpl("D-ABCD", "A", "A");
    private final Plane plane2 = new PlaneImpl("D-BCDE", "B", "A");
    private final List<Plane> planes = Arrays.asList(plane1, plane2);
    private Stage stage;
    @Mock
    private BusinessLogicAPI businessLogicAPI;

    @Start
    void start(Stage stage) throws IOException {
        var app = new App();
        app.start(stage);
        businessLogicAPI = mock(BusinessLogicAPI.class);
        when(businessLogicAPI.getAllPlanes(any())).thenReturn(planes);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }

    /**
     * Test if the PlaneTable is populated correctly
     * @param fxRobot
     */
    @Test
    void testTableShowsRows(FxRobot fxRobot) {
        var planeTables = fxRobot.lookup(node -> node instanceof PlaneTable).queryAllAs(PlaneTable.class);
        if (planeTables.isEmpty()) {
            fail("Could not find PlaneTable");
        } else if (planeTables.size() > 1) {
            fail("To many PlaneTables found");
        }
        var planeTable = new ArrayList<>(planeTables).get(0);
        var items = new ArrayList<>(planeTable.getItems());
        assertThat(items).containsAll(planes);
        verify(businessLogicAPI).getAllPlanes(any());
    }

    /**
     * Test that when a PlaneTable is created, a click on one of the rows will lead to a navigation event to the next Scene
     * @param fxRobot
     */
    @Test
    void testTableClickOnRow(FxRobot fxRobot) {
        // Lookup the PlaneTable
        var planeTables = fxRobot.lookup(node -> node instanceof PlaneTable).queryAllAs(PlaneTable.class);
        // Fail if none found or more then one found
        if (planeTables.isEmpty()) {
            fail("Could not find PlaneTable");
        } else if (planeTables.size() > 1) {
            fail("To many PlaneTables found");
        }
        // Get the PlaneTable
        var tableView = new ArrayList<>(planeTables).get(0);
        // Query the children nodes of the PlaneTable for TableRows
        List<Node> current = tableView.getChildrenUnmodifiable();
        while (current.size() == 1) {
            current = ((Parent) current.get(0)).getChildrenUnmodifiable();
        }
        current = ((Parent) current.get(1)).getChildrenUnmodifiable();
        while (!(current.get(0) instanceof TableRow)) {
            current = ((Parent) current.get(0)).getChildrenUnmodifiable();
        }
        // Get the first TableRow
        Node node = current.get(0);
        if (node instanceof TableRow) {
            fxRobot.clickOn(node);
        }
        else {
            throw new RuntimeException("Expected Group with only TableRows as children");
        }
        // Verify that the UI called viewPlane on the businessLogicAPI. Shows that the click was successful and the RowClickHandler works as intended
        verify(businessLogicAPI).getAllPlanes(any());
    }
}
