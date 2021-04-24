package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.businessLogic.*;
import com.g02.flightsalesfx.persistence.PersistenceAPI;
import com.g02.flightsalesfx.persistence.PersistenceApiImplementationProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Employee employee;
    private static Scene scene;

    static PersistenceAPI persistenceAPI;
    static BusinessLogicAPI businessLogicAPI;
    static int inRootTab=0;
    static boolean comesFromCreateFlight = false;

    @Override
    public void start(Stage stage) throws IOException {
        persistenceAPI = PersistenceApiImplementationProvider.getImplementation();
        businessLogicAPI = BusinessLogicImplementationProvider.getImplementation(persistenceAPI);
        scene = new Scene(loadFXML("login"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Flight Ticket Sales");
        stage.show();
    }


    static void setRoot(String fxml) {
        try {
            Parent p = loadFXML(fxml);
        scene.setRoot(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}