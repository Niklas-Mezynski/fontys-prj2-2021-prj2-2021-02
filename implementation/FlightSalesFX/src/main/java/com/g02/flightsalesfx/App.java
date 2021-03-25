package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.BusinessLogicImplementationProvider;
import com.g02.flightsalesfx.persistence.PersistenceAPI;
import com.g02.flightsalesfx.persistence.PersistenceApiImplementationProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Employee employee;
    private static Scene scene;

    static PersistenceAPI persistenceAPI;
    static BusinessLogicAPI businessLogicAPI;

    @Override
    public void start(Stage stage) throws IOException {
        persistenceAPI = PersistenceApiImplementationProvider.getImplementation();
        businessLogicAPI = BusinessLogicImplementationProvider.getImplementation(persistenceAPI);
        scene = new Scene(loadFXML("createPlane"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Flight Ticket Sales");

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}