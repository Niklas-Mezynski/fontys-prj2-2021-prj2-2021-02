package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Employee;
import org.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import org.g02.flightsalesfx.businessLogic.BusinessLogicImplementationProvider;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;
import org.g02.flightsalesfx.persistence.PersistenceAPI;
import org.g02.flightsalesfx.persistence.PersistenceApiImplementationProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Employee employee;
    static PersistenceAPI persistenceAPI;
    static BusinessLogicAPI businessLogicAPI;
    static int inRootTab = 0;
    static boolean comesFromCreateFlight = false;
    private static Scene scene;

    static void setRoot(String fxml) {
        setRoot(fxml, new Bundle());
    }

    static void setRoot(String fxml, Bundle bundle) {
        try {
            var loader = loadFXML(fxml);
            var p = (Parent) loader.load();
            var controller = (Controller) loader.getController();
            controller.init(bundle);
            scene.setRoot(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        persistenceAPI = PersistenceApiImplementationProvider.getImplementation();
        businessLogicAPI = BusinessLogicImplementationProvider.getImplementation(persistenceAPI);
        scene = new Scene(loadFXML("login").load(), 800, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Flight Ticket Sales");
        stage.show();
    }

}