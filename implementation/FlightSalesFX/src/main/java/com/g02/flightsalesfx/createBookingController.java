package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.*;
import com.g02.flightsalesfx.gui.FlightTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class createBookingController {

    @FXML
    private Tab flightTab;

    @FXML
    private VBox flightVBox;

    @FXML
    private TextField departureField;

    @FXML
    private TextField arrivalField;

    @FXML
    private Tab seatsTab;

    @FXML
    private VBox filterVBox;

    @FXML
    private HBox seatHBox;

    @FXML
    private Tab paxTab;

    @FXML
    private VBox paxListVBox;

    @FXML
    private Text availableSeatsText;

    @FXML
    private Tab flightOptionsTab;

    @FXML
    private VBox flightOptionsVBox;

    @FXML
    private Tab confirmTab;

    @FXML
    private HBox flightOverview;

    @FXML
    private HBox flightOptionsOverview;

    @FXML
    private HBox contactEmailOverview;

    @FXML
    private VBox seatsOverviewVBox;

    @FXML
    private Text selectedFlightText;

    public void generateOverview(){
        flightOverview.getChildren().add(new Label("FlightNo: "+selectedFlight.getFlightNumber()+"; From: "+selectedFlight.getRoute().getDepartureAirport().toString()+"; To: "+selectedFlight.getRoute().getArrivalAirport().toString()+"; On: "+selectedFlight.getDeparture().toString()));

        flightOptionsOverview.getChildren().add(new Label(selectedFlightOptions.toString()));
        contactEmailOverview.getChildren().add(new Label(contactEmailField.getText()));
    }


    private FlightTable flightTable;

    private Flight selectedFlight = null;

    private Map<Seat,List<SeatOption>> selectedSeatsForBooking = new HashMap<Seat,List<SeatOption>>();

    private List<SeatBookButton> seatButtons = new ArrayList<SeatBookButton>();

    private List<SeatOption> currentlySelectedSeatOption = new ArrayList<SeatOption>();

    private List<SeatOption> availableSeatOptions = new ArrayList<SeatOption>();

    private VBox filterContainer;

    private List<PaxInfoBox> paxInfoBoxes = new ArrayList<>();

    private TextField contactEmailField;

    private List<FlightOption> availableFlightOptions = new ArrayList<>();

    private List<FlightOptionSelector> flightOptionSelectors;

    private List<FlightOption> selectedFlightOptions = new ArrayList<>();

    public void initialize(){

        createOrUpdateRouteTable(v -> true);
        createSearchFunctionality();

        paxTab.setDisable(true);
        seatsTab.setDisable(true);
        flightOptionsTab.setDisable(true);
        confirmTab.setDisable(true);

    }

    public void createSearchFunctionality(){
        departureField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateRouteTable(f -> {
                String depField = newValue.toLowerCase();
                String arrField = arrivalField.getText().toLowerCase();
                String dep = f.getRoute().getDepartureAirport().toString().toLowerCase();
                String arr = f.getRoute().getArrivalAirport().toString().toLowerCase();
                return dep.contains(depField)&&arr.contains(arrField);
            });
        }));
        arrivalField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateRouteTable(f -> {
                String depField = departureField.getText().toLowerCase();
                String arrField = newValue.toLowerCase();
                String dep = f.getRoute().getDepartureAirport().toString().toLowerCase();
                String arr = f.getRoute().getArrivalAirport().toString().toLowerCase();
                return dep.contains(depField)&&arr.contains(arrField);
            });
        }));

    }

    public void createOrUpdateOptionFilterBox(){
        filterVBox.getChildren().remove(filterContainer);

        filterContainer = new VBox();
        filterContainer.getChildren().add(new Label("Available Seatoptions:"));
        availableSeatOptions.forEach(seatOption -> {filterContainer.getChildren().add(new OptionFilterSelectorBox(seatOption));});
        filterVBox.getChildren().add(filterContainer);
    }

    public class OptionFilterSelectorBox extends HBox{
        private final SeatOption seatOption;
        public OptionFilterSelectorBox(SeatOption s){
            this.seatOption = s;
            CheckBox c = new CheckBox();
            c.setOnAction(actionEvent -> {
                if(c.isSelected()){
                    if(!currentlySelectedSeatOption.contains(seatOption)){
                        currentlySelectedSeatOption.add(seatOption);
                    }
                }else{
                    currentlySelectedSeatOption.remove(seatOption);
                }
                applyOptionFilterToAll();
            });
            Label l = new Label(s.getName()+ ", Price: "+ "TODO: createBookingController.OptionFilterSelectorBox");
            this.getChildren().addAll(c,l);
        }

    }

    public void createOrUpdateRouteTable(Predicate<Flight> pr){

        flightVBox.getChildren().remove(flightTable);


        var allFlights = App.businessLogicAPI.getAllFlights(pr);

        flightTable = new FlightTable(allFlights, (event, row) -> {
            if (!row.isEmpty()) {
                Flight rowData = row.getItem();

                if (event.getClickCount() == 1) {
                    System.out.println("Selected FLight: " + rowData.toString());
                    selectedFlight = rowData;
                    seatsTab.setDisable(false);
                    selectedFlightText.setText("FlightNo: "+rowData.getFlightNumber()+"; From: "+rowData.getRoute().getDepartureAirport().toString()+"; To: "+rowData.getRoute().getArrivalAirport().toString()+"; On: "+rowData.getDeparture().toString());
                    availableSeatsText.setText(rowData.getPlane().getSeatCount()+"");
                    row.getTableView().refresh();
                    List<Seat> bookedSeats = new ArrayList<Seat>();
                    App.businessLogicAPI.getAllBookings(booking -> true).stream().filter(booking -> booking.getFlight().equals(this.selectedFlight)).forEach(booking ->{
                        booking.getTickets().forEach(ticket -> bookedSeats.add(ticket.getSeat()));
                    });
                    createSeatMapAndLoadSeatOptions(bookedSeats);

                }
            }
        });
        flightVBox.getChildren().add(flightTable);
    }

    public void createSeatMapAndLoadSeatOptions(List<Seat> bookedSeats){
        Plane currentPlane = selectedFlight.getPlane();
        List<Seat> seatsOfPlane = currentPlane.getAllSeats();
        loadSeatOptions(seatsOfPlane);
        createOrUpdateOptionFilterBox();
        int rows = seatsOfPlane.stream().mapToInt(Seat::getRowNumber).max().orElse(-1) + 1;
        Map<Integer, List<Seat>> seatMap = new HashMap<Integer, List<Seat>>();

        for(int i = 0; i < rows; i++){
            int currentRow = i;
            Comparator<Seat> comparator = (Seat s1, Seat s2) -> s1.getSeatNumber()- s2.getSeatNumber();
            List<Seat> row = seatsOfPlane.stream().filter(seat -> seat.getRowNumber()==currentRow).sorted(comparator).collect(Collectors.toUnmodifiableList());
            seatMap.put(i, row);
            VBox rowBox = new VBox();
            seatsOfPlane.stream().filter(seat -> seat.getRowNumber()==currentRow).sorted(comparator).forEach(seat -> {
                var addButton = new SeatBookButton(seat, bookedSeats);
                seatButtons.add(addButton);
                rowBox.getChildren().add(addButton);
            });

            seatHBox.getChildren().add(rowBox);
        }


    }

    public void loadSeatOptions(List<Seat> seatsOfPlane){
        availableSeatOptions = new ArrayList<SeatOption>();
        for(Seat s : seatsOfPlane){
            s.getSeatOptions().forEach(newSeatOption -> {
                if(!availableSeatOptions.stream().anyMatch(seatOption -> seatOption.equals(newSeatOption))){
                    availableSeatOptions.add(newSeatOption);
                }
            });


        }
    }

    public void applyOptionFilterToAll(){
        seatButtons.forEach(btn -> btn.optionFilter(this.currentlySelectedSeatOption));
    }


    public class SeatBookButton extends Button {

        private final Seat s;
        private boolean available = true;

        public SeatBookButton(Seat s, List<Seat> bookedSeats) {
            this.s = s;
            this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            this.setFont(Font.font("Source Code Pro Semibold"));
            setOnAction(actionEvent -> {
                if(selectedSeatsForBooking.containsKey(s)){
                    this.setStyle("");
                    selectedSeatsForBooking.remove(s);
                    if(selectedSeatsForBooking.isEmpty()){
                        paxTab.setDisable(true);
                    }
                }else{
                    this.setStyle("-fx-text-fill: #ff6600; ");
                    List<SeatOption> bookedOptions = currentlySelectedSeatOption.stream().collect(Collectors.toUnmodifiableList());
                    selectedSeatsForBooking.put(s, bookedOptions);
                    if(!selectedSeatsForBooking.isEmpty()){
                        paxTab.setDisable(false);
                    }
                }
            });
            if(bookedSeats.contains(this.s)){
                available = false;
            }
            this.setDisable(!available);
            updateText();

        }



        void optionFilter(List<SeatOption> selectedOptions){
            if(!s.getSeatOptions().containsAll(selectedOptions)){
                this.setDisable(true);
            }else {
                this.setDisable(false);
            }
        }


        /**
         * Updates the text and converts row and seat number to a better readable string consisting of
         */
        void updateText() {
            var i = s.getSeatNumber();
            var i1 = s.getRowNumber() + 1;
            String s = String.format("%02d", i1);
            if (i < 26) {
                s += String.valueOf((char) (i + 65));
            } else {
                s += String.valueOf((char) ((i / 26) + 64));
                s += String.valueOf((char) (i + 65 - 26));
            }
            this.setText(s);
        }


    }

    @FXML
    void abortButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("salesEmployeeHome");
    }

    @FXML
    void disableFlightOptionsTab() {
        flightOptionsTab.setDisable(true);
        generateOverview();
    }

    @FXML
    void disableFlightSelectionTab() {
        flightTab.setDisable(true);
    }

    @FXML
    void disablePaxTab() {
        paxTab.setDisable(true);
        loadFlightOptionsAndSelector();
    }

    public void loadFlightOptionsAndSelector(){
        List<Seat> bookedSeats = new ArrayList<Seat>();
        App.businessLogicAPI.getAllBookings(booking -> true).stream().filter(booking -> booking.getFlight().equals(this.selectedFlight)).forEach(booking ->{
            booking.getTickets().forEach(ticket -> bookedSeats.add(ticket.getSeat()));
        });
        List<FlightOption> alreadyBookedFlightOptions = new ArrayList<FlightOption>();
        App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().equals(this.selectedFlight)).forEach(booking -> booking.getBookedFlightOptions().forEach(bookedFlightOpt -> alreadyBookedFlightOptions.add(bookedFlightOpt)));

        confirmTab.setDisable(false);
        availableFlightOptions = selectedFlight.getFlightOptions().stream().collect(Collectors.toUnmodifiableList());
        VBox flightOptionSelectorContainer = new VBox();
        flightOptionSelectors = new ArrayList<FlightOptionSelector>();
        availableFlightOptions.forEach(flightOption -> {
            int available = flightOption.getMaxAvailability() - (int)alreadyBookedFlightOptions.stream().filter(bookedOption -> bookedOption.equals(flightOption)).count();
            FlightOptionSelector fos = new FlightOptionSelector(flightOption, available);
            flightOptionSelectors.add(fos);
            flightOptionSelectorContainer.getChildren().add(fos);
        });
        flightOptionsVBox.getChildren().add(flightOptionSelectorContainer);
    }

    public void updateSelectedFlightOptions(){
        selectedFlightOptions = new ArrayList<>();
        for(FlightOptionSelector fos : flightOptionSelectors){

            for(int i = 0; i < fos.getSelected(); i++){
                System.out.println("Adding FlightOption: "+ fos.getFlightOption());
                selectedFlightOptions.add(fos.getFlightOption());
            }
        }
        System.out.println(availableFlightOptions+"     Selected:"+selectedFlightOptions);
    }

    public class FlightOptionSelector extends HBox{

        private final FlightOption fOption;
        private Spinner<Integer> cBox;
        public FlightOptionSelector(FlightOption f, int available){
            this.fOption = f;
             cBox = new Spinner<Integer>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, available));
             cBox.valueProperty().addListener((obs, oldValue, newValue) ->updateSelectedFlightOptions());
            this.getChildren().addAll(cBox, new Label(fOption.getName()+" Price: "+fOption.getPrice()+"€"));
        }

        public int getSelected(){
            return cBox.getValue();
        }

        public FlightOption getFlightOption() {
            return fOption;
        }
    }

    @FXML
    void disableSeatAllocationTab() {
        seatsTab.setDisable(true);
        paxInfoBoxes = new ArrayList<PaxInfoBox>();
        for(Seat s: selectedSeatsForBooking.keySet()){
            PaxInfoBox paxInfoBox = new PaxInfoBox(s);
            paxListVBox.getChildren().add(paxInfoBox);
            paxInfoBoxes.add(paxInfoBox);
        }
        contactEmailField = new TextField();
        contactEmailField.setPromptText("E-Mail");
        contactEmailField.textProperty().addListener((observableValue, oldValue, newValue) -> checkPaxInfoStatus());
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("E-Mail for Contacting: "), contactEmailField);
        paxListVBox.getChildren().add(hBox);
    }
    public void checkPaxInfoStatus(){
        if(paxInfoBoxes != null && !paxInfoBoxes.isEmpty() && contactEmailField != null){
            boolean infoMissing = false;
            for(PaxInfoBox p : paxInfoBoxes){
                System.out.println("Name: "+p.getFirstName()+ " " +p.getLastName());
                if(p.getFirstName().equals("") || p.getLastName().equals("")){
                    infoMissing = true;
                }
            }
            if(contactEmailField.getText().equals("")){
                infoMissing = true;
            }
            if(infoMissing)
                flightOptionsTab.setDisable(true);
            else
                flightOptionsTab.setDisable(false);
        }
    }

    public class PaxInfoBox extends HBox{
        private final Seat seat;
        private TextField firstName;
        private TextField lastName;

        public PaxInfoBox(Seat s){
            this.seat = s;
            firstName = new TextField();
            firstName.setPromptText("First Name");
            firstName.textProperty().addListener((observableValue, oldValue, newValue) -> checkPaxInfoStatus());
            lastName = new TextField();
            lastName.setPromptText("Last Name");
            lastName.textProperty().addListener((observableValue, oldValue, newValue) -> checkPaxInfoStatus());
            this.getChildren().addAll(new Label("Passenger for Seat "+ seatToText()), firstName, lastName);
        }

        String getFirstName(){
            return firstName.getText();
        }
        String getLastName(){
            return lastName.getText();
        }
        Seat getSeat(){
            return seat;
        }

        String seatToText() {
            var i = seat.getSeatNumber();
            var i1 = seat.getRowNumber() + 1;
            String s = String.format("%02d", i1);
            if (i < 26) {
                s += String.valueOf((char) (i + 65));
            } else {
                s += String.valueOf((char) ((i / 26) + 64));
                s += String.valueOf((char) (i + 65 - 26));
            }
            return s;
        }
    }

}
