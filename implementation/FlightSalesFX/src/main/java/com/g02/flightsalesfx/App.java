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
        addSamples();
        scene = new Scene(loadFXML("login"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Flight Ticket Sales");
        stage.show();
    }

    public void addSamples(){
        var airportMgr = businessLogicAPI.getAirportManager();
        var routeMgr = businessLogicAPI.getRouteManager();
        var flightMgr = businessLogicAPI.getFlightManager();

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




        //Sample Seats with seatoptions
        SeatOption firstClass = new SeatOptionImpl("First Class");
        SeatOption emergencyExitRow = new SeatOptionImpl("More Space");
        List<Seat> seats2 = new ArrayList<>();
        for(int i = 0; i <=4; i++){
            for(int y = 0; y <=3 ; y++){
                Seat s = new SeatImpl(i,y);
                s.addSeatOption(firstClass);
                seats2.add(s);
            }
        }
        for(int i = 5; i < 35; i++){
            for(int y = 0; y <=5 ; y++){
                Seat s = new SeatImpl(i,y);
                if(i == 20 || i == 21){
                    s.addSeatOption(emergencyExitRow);
                }
                seats2.add(s);

            }
        }
        //Sample Planes
        List<Seat> seats = new ArrayList<>();
        seats.add(new SeatImpl(0, 0));
        seats.add(new SeatImpl(0, 1));
        seats.add(new SeatImpl(1, 0));
        businessLogicAPI.createPlaneFromUI("D-ABCH", "A069", "Airbus", seats);
        businessLogicAPI.createPlaneFromUI("B-VRTC", "B420", "QualityPlanes", seats);
        businessLogicAPI.createPlaneFromUI("D-SLKD", "B738", "Boeing", seats2);

        var routes = businessLogicAPI.getAllRoutes(t -> true);
        var planes = businessLogicAPI.getAllPlanes(t -> true);
        businessLogicAPI.createFlightFromUI((SalesOfficer) App.employee, 4485, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(4).plusMinutes(34), routes.get(3), planes.get(0), 99.34 );
        businessLogicAPI.createFlightFromUI((SalesOfficer) App.employee, 4486, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2).plusMinutes(44), routes.get(12), planes.get(1), 45.65 );
        businessLogicAPI.createFlightFromUI((SalesOfficer) App.employee, 4487, LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(7).plusHours(4).plusMinutes(34), routes.get(3), planes.get(0), 78.23 );
        businessLogicAPI.createFlightFromUI((SalesOfficer) App.employee, 4488, LocalDateTime.now().plusDays(8), LocalDateTime.now().plusDays(8).plusHours(2).plusMinutes(44), routes.get(12), planes.get(1), 43.56 );
        businessLogicAPI.createFlightFromUI((SalesOfficer) App.employee, 4489, LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(9).plusHours(4).plusMinutes(34), routes.get(3), planes.get(0), 34.86 );
        businessLogicAPI.createFlightFromUI((SalesOfficer) App.employee, 4490, LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(9).plusHours(2).plusMinutes(44), routes.get(14), planes.get(2), 67.27 );
        var flights = persistenceAPI.getFlightStorageService(businessLogicAPI.getFlightManager()).getAll();
        var flightOptions = new ArrayList<FlightOption>();
        flightOptions.add(new FlightOptionImpl("Currywurst", 2, 5.50));
        flightOptions.add(new FlightOptionImpl("Tomatensaft",3, 2.20));
        flights.forEach( flight -> flight.addAllFlightOptions(flightOptions));


    }

    static void setRoot(String fxml) throws IOException {
        Parent p = loadFXML(fxml);
        scene.setRoot(p);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}