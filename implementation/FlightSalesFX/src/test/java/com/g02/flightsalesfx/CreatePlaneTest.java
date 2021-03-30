package com.g02.flightsalesfx;

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
        this.stage=stage;
    }
    @Test
    void createPlaneOpens(FxRobot nils) {
        //See the comment above if this produces a weird error
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        Parent rootOfElements = stage.getScene().getRoot();
        Assertions.assertThat(nils.lookup("#goToCreatePlane").queryAs(Button.class)).hasText("Create Plane");
        nils.clickOn(nils.lookup("#goToCreatePlane").queryAs(Button.class));
        Assertions.assertThat(nils.lookup(".label").queryAs(Label.class)).hasText("Create Plane");
    }

}
