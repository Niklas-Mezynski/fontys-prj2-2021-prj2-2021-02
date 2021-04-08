package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Employee;
import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.businessLogic.AirportImpl;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPI;
import com.g02.flightsalesfx.businessLogic.BusinessLogicImplementationProvider;
import com.g02.flightsalesfx.businessLogic.SeatImpl;
import com.g02.flightsalesfx.persistence.PersistenceAPI;
import com.g02.flightsalesfx.persistence.PersistenceApiImplementationProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
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
        addSamples();
        scene = new Scene(loadFXML("login"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Flight Ticket Sales");
        stage.show();
    }

    public void addSamples(){
        var airportMgr = businessLogicAPI.getAirportManager();

        var routeMgr = businessLogicAPI.getRouteManager();

        businessLogicAPI.createAirportFromUI("BRE","Bremen","Germany");
        businessLogicAPI.createAirportFromUI("FCN","Cuxhaven","Germany");
        businessLogicAPI.createAirportFromUI("DRS","Dresden","Germany");
        businessLogicAPI.createAirportFromUI("HHN","Frankfurt-Hahn","Germany");
        businessLogicAPI.createAirportFromUI("HAM","Hamburg","Germany");
        businessLogicAPI.createAirportFromUI("DUS", "Düsseldorf", "Germany");
        businessLogicAPI.createAirportFromUI("BER", "Berlin", "Germany");
        businessLogicAPI.createAirportFromUI("FRA", "Frankfurt", "Germany");

        var airports = businessLogicAPI.getAllAirports(ap -> true);
        for(int i = 0; i < airports.size(); i++){
            for(int y = 0; y < airports.size(); y++){
                if(i!=y){
                    businessLogicAPI.createRouteFromUI(airports.get(i), airports.get(y));
                }
            }
        }

        //Sample Planes
        List<Seat> seats = new ArrayList<>();
        seats.add(new SeatImpl(0, 0));
        seats.add(new SeatImpl(0, 1));
        seats.add(new SeatImpl(1, 0));
        businessLogicAPI.createPlaneFromUI("D-ABCH", "A069", "Airbus", seats);
        businessLogicAPI.createPlaneFromUI("B-VRTC", "B420", "QualityPlanes", seats);

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