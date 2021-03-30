package com.g02.flightsalesfx;

import javafx.scene.Parent;
import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Add "--add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED" to you launch arguments if this produces errors
 */


public class CreatePlaneTest extends ApplicationTest {
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        var app=new App();
        app.start(stage);
        this.stage=stage;
    }
    @Test
    public void uiTest() {
        assertThat(stage.getTitle()).isEqualTo("Flight Ticket Sales");
        Parent rootOfElements = stage.getScene().getRoot();

    }

}
