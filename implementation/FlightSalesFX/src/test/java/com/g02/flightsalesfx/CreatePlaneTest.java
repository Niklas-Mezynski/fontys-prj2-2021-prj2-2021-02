package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.businessLogic.PlaneImpl;
import com.g02.flightsalesfx.businessLogic.SeatImpl;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.assertions.api.Assertions.assertThat;

/**
 * Add "--add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED" to you launch arguments if this produces errors
 */

@ExtendWith(ApplicationExtension.class)
public class CreatePlaneTest {
    private Stage stage;

    static {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }

    @Start
    void start(Stage stage) throws IOException {
        var app = new App();
        app.start(stage);
        App.setRoot("home");
        this.stage = stage;
    }

    @Test
    void createPlaneOpens(FxRobot nils) {
        //See the comment above if this produces a weird error
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        assertThat(nils.lookup(".label").queryAs(Label.class)).doesNotHaveText("Create Plane");
        assertThat(nils.lookup("#goToCreatePlane").queryAs(Button.class)).hasText("Create Plane");
        nils.clickOn(nils.lookup("#goToCreatePlane").queryAs(Button.class));
        //Now in createPlane
        assertThat(nils.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
    }

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
        fxRobot.clickOn(fxRobot.lookup("#savePlaneButton").queryButton());
        var planes = App.persistenceAPI.getPlaneStorageService(App.businessLogicAPI.getPlaneManager()).getAll();
        var plane = new PlaneImpl("D-AGVD", "Airbus", "A380");
        plane.addSeat(new SeatImpl(0, 0));
        plane.addSeat(new SeatImpl(0, 1));
        assertThat(planes).element(0).isEqualTo(plane);
    }

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
        
        @Test
    void seatOptionAddingRemovingTest(FxRobot nils) throws IOException {
        nils.clickOn(nils.lookup("#goToCreatePlane").queryAs(Button.class));
        Assertions.assertThat(nils.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
        nils.clickOn(nils.lookup("#addRow").queryAs(Button.class));
        var buttons=nils.lookup((s)-> s instanceof Button).queryAllAs(Button.class);
        Button addButton=null;
        for (Button button : buttons) {
            if (button.getText().equals("ADD")){
                addButton=button;
            }
        }
        nils.clickOn(addButton);
        buttons=nils.lookup((s)-> s instanceof Button).queryAllAs(Button.class);
        Button seatButton=null;
        for (Button button : buttons) {
            if (button.getText().equals("01A")){
                seatButton=button;
            }
        }
        var p=seatButton.getTextFill();
        nils.clickOn(nils.lookup("#addSeatOptionButton").queryButton());
        var seatOptionButtons=nils.lookup(b->b instanceof ToggleButton).queryAs(ToggleButton.class);
        assertThat(seatOptionButtons).isNotNull();
        nils.clickOn(seatOptionButtons);
        nils.clickOn(seatButton);
        assertThat(seatButton.getTextFill()).isNotEqualTo(p);
        nils.clickOn(seatButton);
        assertThat(seatButton.getTextFill()).isEqualTo(p);
        nils.clickOn(seatButton);
        assertThat(seatButton.getTextFill()).isNotEqualTo(p);
        nils.clickOn(seatOptionButtons);
        assertThat(seatButton.getTextFill()).isEqualTo(p);
        nils.clickOn(seatOptionButtons);
        assertThat(seatButton.getTextFill()).isNotEqualTo(p);
    }

}
