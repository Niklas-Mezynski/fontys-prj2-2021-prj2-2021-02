package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.NodeQuery;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.testfx.assertions.api.Assertions.assertThat;

/**
 * Add "--add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED" to you launch arguments if this produces errors
 */
//@Disabled
@ExtendWith(ApplicationExtension.class)
public class CreatePlaneTest {
    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }

    private Stage stage;

    @Mock
    private BusinessLogicAPI businessLogicAPI;

    /**
     * Setting up the test.
     * Create the mocks and inject it into the App
     * Skip Login and move to the home page
     * @param stage
     * @throws IOException if home.fxml can't be found
     */
    @Start
    void start(Stage stage) throws IOException {
        var app = new App();
        app.start(stage);
        businessLogicAPI = Mockito.mock(BusinessLogicAPI.class);
        App.businessLogicAPI = businessLogicAPI;
        App.setRoot("home");
        this.stage = stage;
    }

    /**
     * Test if the createPlane scene can be opened from the homepage
     * @param fxRobot
     */
    @Test
    void createPlaneOpens(FxRobot fxRobot) {
        //See the comment above if this produces a weird error
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        assertThat(fxRobot.lookup(".label").queryAs(Label.class)).doesNotHaveText("Create Plane");
        assertThat(fxRobot.lookup("#goToCreatePlane").queryAs(Button.class)).hasText("Create Plane");
        fxRobot.clickOn(fxRobot.lookup("#goToCreatePlane").queryAs(Button.class));
        //Now in createPlane
        assertThat(fxRobot.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
    }

    /**
     * Test that a new row and seat can be added on the UI
     * @param nils
     */
    @Test
    void addRowAndSeatWorks(FxRobot nils) {
        nils.clickOn(nils.lookup("#goToCreatePlane").queryAs(Button.class));
        //Now in createPlane
        assertThat(nils.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
        nils.clickOn(nils.lookup("#addRow").queryAs(Button.class));
        var buttons = nils.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        var a = false;
        Button addButton = null;
        for (Button button : buttons) {
            if (button.getText().equals("ADD")) {
                a = true;
                addButton = button;
            }
        }
        assertThat(a).isTrue();
        nils.clickOn(addButton);
        buttons = nils.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        a = false;
        for (Button button : buttons) {
            if (button.getText().equals("01A")) {
                a = true;
            }
        }
        assertThat(a).isTrue();
    }

    /**
     * Test that the UI calls the createPlaneFromUI method of the businessLogicAPI by mocking it
     * @param fxRobot
     */
    @Test
    void testSavePlane(FxRobot fxRobot) {
        fxRobot.clickOn(fxRobot.lookup("#goToCreatePlane").queryAs(Button.class));
        assertThat(fxRobot.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
        fxRobot.clickOn(fxRobot.lookup("#addRow").queryAs(Button.class));
        var buttons = fxRobot.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        for (Button button : buttons) {
            if (button.getText().equals("ADD")) {
                fxRobot.clickOn(button);
                fxRobot.clickOn(button);
            }
        }
        var planeName = fxRobot.lookup("#planeName").queryAs(TextField.class);
        fxRobot.clickOn(planeName);
        fxRobot.write("D-AGVD", 1);
        var planeType = fxRobot.lookup("#planeType").queryAs(TextField.class);
        fxRobot.clickOn(planeType);
        fxRobot.write("A380");
        var planeManufacturer = fxRobot.lookup("#planeManufacturer").queryAs(TextField.class);
        fxRobot.clickOn(planeManufacturer);
        fxRobot.write("Airbus");
        Mockito.when(businessLogicAPI.createPlaneFromUI(any(), any(), any(), any())).thenReturn(new PlaneImpl("D-AGVD", "A380", "Airbus"));
        fxRobot.clickOn(fxRobot.lookup("#savePlaneButton").queryButton());
        Mockito.verify(businessLogicAPI, Mockito.times(1)).createPlaneFromUI(any(), any(), any(), any());
    }

    /**
     * Test that if there is an error creating the plane an alert dialog is shown. Also using a mock of the businessLogic
     * @param fxRobot
     */
    @Test
    void savePlaneErrorAlertDialog(FxRobot fxRobot) {
        fxRobot.clickOn(fxRobot.lookup("#goToCreatePlane").queryAs(Button.class));
        Mockito.when(businessLogicAPI.createPlaneFromUI(any(), any(), any(), any())).thenReturn(new PlaneImpl("D-AGVD", "A380", "Airbus"));
        fxRobot.clickOn(fxRobot.lookup("#savePlaneButton").queryButton());
        Node dialogPane = fxRobot.lookup(".dialog-pane").queryAs(DialogPane.class);
        var are_you_sure = fxRobot.from(dialogPane).lookup((Text t) -> t.getText().startsWith("There was an error while saving the created plane. Try again!"));
        assertThat(are_you_sure.queryAll()).isNotEmpty();
        for (Button queryAllA : fxRobot.from(dialogPane).lookup((Node node) -> node instanceof Button).queryAllAs(Button.class)) {
            System.out.println(queryAllA.getText());
            if (queryAllA.getText().equals("OK")) {
                fxRobot.clickOn(queryAllA);
            }
        }
    }

    /**
     * Test that the SeatButtons have the correct labels
     * @param fxRobot
     */
    @Test
    void testSeatButtonLabelAfter26Seats(FxRobot fxRobot) {
        fxRobot.clickOn(fxRobot.lookup("#goToCreatePlane").queryAs(Button.class));
        assertThat(fxRobot.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
        fxRobot.clickOn(fxRobot.lookup("#addRow").queryAs(Button.class));
        var buttons = fxRobot.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        for (Button button : buttons) {
            if (button.getText().equals("ADD")) {
                fxRobot.clickOn(button);
                for (int i = 0; i < 26; i++) {
                    fxRobot.press(KeyCode.SPACE);
                    fxRobot.release(KeyCode.SPACE);
//                    button.fire();
               /*     if (i > 10)
                        fxRobot.scroll(10, VerticalDirection.DOWN);*/
                }
            }
        }
        var nodes = fxRobot.lookup(node -> node instanceof CreatePlaneController.SeatButton).queryAllAs(CreatePlaneController.SeatButton.class);
        assertThat(nodes).anyMatch(node -> node.getText().equals("01AA"));
    }

    /**
     * Test that seats can be remove after they were added
     * @param test
     */
    @Test
    void removeSeat(FxRobot test) {
        test.clickOn(test.lookup("#goToCreatePlane").queryAs(Button.class));
        test.clickOn(test.lookup("#addRow").queryAs(Button.class));
        var buttons = test.lookup(s -> s instanceof Button).queryAllAs(Button.class);
        Button addButton = null;
        for (Button button : buttons) {
            if (button.getText().equals("ADD")) {
                addButton = button;
            }
        }
        test.clickOn(addButton);
        test.clickOn(addButton);
        test.clickOn(addButton);
        buttons = test.lookup(s -> s instanceof Button).queryAllAs(Button.class);
        for (Button button : buttons) {
            if (button.getText().equals("01A")) {
                addButton = button;
            }
        }
        test.clickOn(addButton);
        buttons = test.lookup(s -> s instanceof Button).queryAllAs(Button.class);
        Boolean isRemoved = true;
        for (Button button : buttons) {
            if (button.getText().equals("01C")) {
                isRemoved = false;
            }
        }
        assertThat(isRemoved).isTrue();
        test.clickOn(helperGetButtonByName("01B", test).get(0));
        test.clickOn(helperGetButtonByName("01A", test).get(0));
        var emptyList = helperGetButtonByName("ADD", test);
        assertThat(emptyList.isEmpty()).isTrue();

    }

    /**
     * Test that rows can be removed after they were added
     * @param test
     */
    @Test
    void removeRow(FxRobot test) {
        test.clickOn(test.lookup("#goToCreatePlane").queryAs(Button.class));
        test.clickOn(test.lookup("#addRow").queryAs(Button.class));
        test.clickOn(test.lookup("#addRow").queryAs(Button.class));
        for (Button button : helperGetButtonByName("ADD", test)) {
            test.clickOn(button);
            test.clickOn(button);
            test.clickOn(button);
        }
        Button b = helperGetButtonByName("ADD", test).get(1);
        test.clickOn(b);
        test.rightClickOn(b);
        var text = helperGetButtonByName("02B", test);
        assertThat(text).isEmpty();
    }

    /**
     * Test that rows can be cloned and that the new row has as many seats as the row before it
     * @param test
     */
    @Test
    void cloneRow(FxRobot test) {
        test.clickOn(test.lookup("#goToCreatePlane").queryAs(Button.class));
        Assertions.assertThat(test.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
        test.clickOn(test.lookup("#addRow").queryAs(Button.class));

        var buttons = test.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        var a = false;
        Button addButton = null;
        for (Button button : buttons) {
            if (button.getText().equals("ADD")) {
                a = true;
                addButton = button;
            }
        }
        assertThat(a).isTrue();
        test.clickOn(addButton);
        buttons = test.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        a = false;
        for (Button button : buttons) {
            if (button.getText().equals("01A")) {
                a = true;
            }
        }
        assertThat(a).isTrue();
        test.clickOn(addButton);
        test.clickOn(test.lookup("#addRow1").queryAs(Button.class));
        buttons = test.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        a = false;
        for (Button button : buttons) {
            if (button.getText().equals("02B")) {
                a = true;
            }
        }
        assertThat(a).isTrue();
    }

    /**
     * Test that SeatOptions can be added and removed
     * @param fxRobot
     * @throws IOException
     */
    @Test
    void seatOptionAddingRemovingTest(FxRobot fxRobot) throws IOException {
        fxRobot.clickOn(fxRobot.lookup("#goToCreatePlane").queryAs(Button.class));
        Assertions.assertThat(fxRobot.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
        fxRobot.clickOn(fxRobot.lookup("#addRow").queryAs(Button.class));
        var buttons = fxRobot.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        Button addButton = null;
        for (Button button : buttons) {
            if (button.getText().equals("ADD")) {
                addButton = button;
            }
        }
        fxRobot.clickOn(addButton);
        buttons = fxRobot.lookup((s) -> s instanceof Button).queryAllAs(Button.class);
        Button seatButton = null;
        for (Button button : buttons) {
            if (button.getText().equals("01A")) {
                seatButton = button;
            }
        }
        var p = seatButton.getTextFill();
        fxRobot.clickOn(fxRobot.lookup("#addSeatOptionButton").queryButton());
        var seatOptionButtons = fxRobot.lookup(b -> b instanceof ToggleButton).queryAs(ToggleButton.class);
        assertThat(seatOptionButtons).isNotNull();
        fxRobot.clickOn(seatOptionButtons);
        fxRobot.clickOn(seatButton);
        assertThat(seatButton.getTextFill()).isNotEqualTo(p);
        fxRobot.clickOn(seatButton);
        assertThat(seatButton.getTextFill()).isEqualTo(p);
        fxRobot.clickOn(seatButton);
        assertThat(seatButton.getTextFill()).isNotEqualTo(p);
        fxRobot.clickOn(seatOptionButtons);
        assertThat(seatButton.getTextFill()).isEqualTo(p);
        fxRobot.clickOn(seatOptionButtons);
        assertThat(seatButton.getTextFill()).isNotEqualTo(p);
    }

    @Test
    void testPlaneInTable(FxRobot fxRobot) {
        var lookup = fxRobot.lookup("#planeTable");
        System.out.println(lookup);
    }

    List<Button> helperGetButtonByName(String buttonText, FxRobot test) {
        var buttons = test.lookup(s -> s instanceof Button).queryAllAs(Button.class);
        var test2 = buttons.stream().filter(s -> s.getText().equals(buttonText)).collect(Collectors.toList());
        return test2;
    }

}
