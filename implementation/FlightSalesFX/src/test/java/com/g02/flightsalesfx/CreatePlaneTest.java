package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Seat;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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

/**
 * Add "--add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED" to you launch arguments if this produces errors
 */

@ExtendWith(ApplicationExtension.class)
public class CreatePlaneTest {
    private Stage stage;

    @Start
    void start(Stage stage) throws IOException {
        var app=new App();
        app.start(stage);
        App.setRoot("home");
        this.stage=stage;
    }
    @Test
    void createPlaneOpens(FxRobot nils) {
        //See the comment above if this produces a weird error
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        Assertions.assertThat(nils.lookup(".label").queryAs(Label.class)).doesNotHaveText("Create Plane");
        Assertions.assertThat(nils.lookup("#goToCreatePlane").queryAs(Button.class)).hasText("Create Plane");
        nils.clickOn(nils.lookup("#goToCreatePlane").queryAs(Button.class));
        //Now in createPlane
        Assertions.assertThat(nils.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
    }

    @Test
    void addRowAndSeatWorks(FxRobot nils){
        nils.clickOn(nils.lookup("#goToCreatePlane").queryAs(Button.class));
        //Now in createPlane
        Assertions.assertThat(nils.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
        nils.clickOn(nils.lookup("#addRow").queryAs(Button.class));
        var buttons=nils.lookup((s)-> s instanceof Button).queryAllAs(Button.class);
        var a=false;
        Button addButton=null;
        for (Button button : buttons) {
            if (button.getText().equals("ADD")){
                a = true;
                addButton=button;
            }
        }
        assertThat(a).isTrue();
        nils.clickOn(addButton);
        buttons=nils.lookup((s)-> s instanceof Button).queryAllAs(Button.class);
        a=false;
        for (Button button : buttons) {
            if (button.getText().equals("01A")){
                a = true;
            }
        }
        assertThat(a).isTrue();
    }

}
