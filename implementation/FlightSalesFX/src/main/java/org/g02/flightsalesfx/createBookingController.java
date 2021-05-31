package org.g02.flightsalesfx;

import org.g02.flightsalesfx.gui.FlightTable;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.g02.flightsalesfx.businessEntities.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class createBookingController implements Controller {

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
    private HBox totalPriceVBox;

    @FXML
    private VBox seatsOverviewVBox;

    @FXML
    private Text selectedFlightText;

    @FXML
    private TabPane tabPane;


    private double sum = 0.0;

    private FlightTable flightTable;

    private Flight selectedFlight = null;

    private Map<Seat,List<SeatOption>> selectedSeatsForBooking = new HashMap<Seat,List<SeatOption>>();

    private List<SeatBookButton> seatButtons = new ArrayList<SeatBookButton>();

    private List<SeatOption> currentlySelectedSeatOption = new ArrayList<SeatOption>();

    private List<SeatOption> availableSeatOptions = new ArrayList<SeatOption>();

    private VBox filterContainer;

    private List<PaxInfoBox> paxInfoBoxes = new ArrayList<>();

    private TextField contactEmailField;

    private Label invalidEmailLabel;

    private List<FlightOption> availableFlightOptions = new ArrayList<>();

    private List<FlightOptionSelector> flightOptionSelectors;

    private Map<FlightOption, Integer> selectedFlightOptions = new HashMap<>();

    private Map<Seat, Pair<String, String>> personNameSeatComb;

    private String contactEmail = "";

    public void initialize(){

        createOrUpdateRouteTable(v -> v.getSalesProcessStatus() == true && v.getDeparture().isAfter(LocalDateTime.now()));

        createSearchFunctionality();

        paxTab.setDisable(true);
        seatsTab.setDisable(true);
        flightOptionsTab.setDisable(true);
        confirmTab.setDisable(true);

    }

    /**
     * Add listeners to Departure- and arivalTextField -> update FlightTable
     */
    public void createSearchFunctionality(){
        departureField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateRouteTable(f -> {
                String depField = newValue.toLowerCase();
                String arrField = arrivalField.getText().toLowerCase();
                String dep = f.getRoute().getDepartureAirport().toString().toLowerCase();
                String arr = f.getRoute().getArrivalAirport().toString().toLowerCase();
                return dep.contains(depField)&&arr.contains(arrField) && f.getSalesProcessStatus() && f.getDeparture().isAfter(LocalDateTime.now());
            });
        }));
        arrivalField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            createOrUpdateRouteTable(f -> {
                String depField = departureField.getText().toLowerCase();
                String arrField = newValue.toLowerCase();
                String dep = f.getRoute().getDepartureAirport().toString().toLowerCase();
                String arr = f.getRoute().getArrivalAirport().toString().toLowerCase();
                return dep.contains(depField)&&arr.contains(arrField)&&f.getSalesProcessStatus() && f.getDeparture().isAfter(LocalDateTime.now());
            });
        }));

    }

    /**
     * Creates or Updates The Box Containing all availage seatoptions.
     */
    public void createOrUpdateOptionFilterBox(){
        filterVBox.getChildren().remove(filterContainer);

        filterContainer = new VBox();
        filterContainer.getChildren().add(new Label("Available Seatoptions:"));
        availableSeatOptions.forEach(seatOption -> {filterContainer.getChildren().add(new OptionFilterSelectorBox(seatOption));});
        filterVBox.getChildren().add(filterContainer);
    }


    /**
     * Represents a SeatOption in the OptionFilterBox
     * When selected, SeatOption is added to "currentlySelectedSeatOption"
     * When deselected, Seatoption is removed from "currentlySelectedSeatOption"
     * method "applyOptionFilterToAll()" called, to disable Seats from being selected, when they dont match selected SeatOptions
     */
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
            Label l = new Label(s.getName()+ ", Price: "+ s.getPrice());
            this.getChildren().addAll(c,l);
        }

    }

    /**
     *
     * creates or updates Route Table
     * @param pr predicate, which has to be true for a Flight, in order to be displayed in the table
     */
    public void createOrUpdateRouteTable(Predicate<Flight> pr){

        flightVBox.getChildren().remove(flightTable);


        var allFlights = App.businessLogicAPI.getAllFlights(pr);

        flightTable = new FlightTable(allFlights, (event, row) -> {
            if (!row.isEmpty()) {
                Flight rowData = row.getItem();

                if (event.getClickCount() == 1) {
                    System.out.println("Selected Flight: " + rowData.toString());
                    selectedFlight = rowData;
                    seatsTab.setDisable(false);
                    selectedFlightText.setText("FlightNo: "+rowData.getFlightNumber()+"; From: "+rowData.getRoute().getDepartureAirport().toString()+"; To: "+rowData.getRoute().getArrivalAirport().toString()+"; On: "+rowData.getDeparture().toString());
                    availableSeatsText.setText(rowData.getPlane().getSeatCount()+"");
                    row.getTableView().refresh();
                    List<Seat> bookedSeats = new ArrayList<Seat>();
                    App.businessLogicAPI.getAllBookings(booking -> booking.getFlight().getFlightNumber() == selectedFlight.getFlightNumber()).stream().forEach(booking -> {
                        System.out.println("Found Booking");
                        booking.getTickets().forEach(ticket -> bookedSeats.add(ticket.getSeat()));
                    });
                    tabPane.getSelectionModel().select(seatsTab);
                    createSeatMapAndLoadSeatOptions(bookedSeats);

                }
            }
        });
        flightVBox.getChildren().add(flightTable);
    }

    /**
     * creates a graphical overview of available seats, that can be selected
     * @param bookedSeats List of Seats, that are already booked, thus not available for new bookings
     */
    public void createSeatMapAndLoadSeatOptions(List<Seat> bookedSeats){
        seatHBox.getChildren().clear();
        Plane currentPlane = selectedFlight.getPlane();
        List<Seat> seatsOfPlane = currentPlane.getAllSeats();
        loadSeatOptions(seatsOfPlane);
        createOrUpdateOptionFilterBox();
        int rows = seatsOfPlane.stream().mapToInt(Seat::getRowNumber).max().orElse(-1) + 1;

        for(int i = 0; i < rows; i++){
            System.out.println(i);
            int currentRow = i;
            Comparator<Seat> comparator = (Seat s1, Seat s2) -> s1.getSeatNumber()- s2.getSeatNumber();
            VBox rowBox = new VBox();
            seatsOfPlane.stream().filter(seat -> seat.getRowNumber()==currentRow).sorted(comparator).forEach(seat -> {
                var addButton = new SeatBookButton(seat, bookedSeats);
                seatButtons.add(addButton);
                rowBox.getChildren().add(addButton);
            });

            seatHBox.getChildren().add(rowBox);
        }


    }

    /**
     * loads all available seatoptions on that plane
     * @param seatsOfPlane all seats of the plane
     */
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

    /**
     * applies the selected seatOptions to all SeatButtons
     */
    public void applyOptionFilterToAll(){
        seatButtons.forEach(btn -> btn.optionFilter(this.currentlySelectedSeatOption));
    }

    /**
     * represents a Seat as a Button in the SeatMap
     * Button state is disabled, when the seat is already booked, or is not qualified for the selected Seatoptions
     */
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
            if(bookedSeats.stream().anyMatch(seat -> (seat.getRowNumber() == this.s.getRowNumber() && seat.getSeatNumber() == this.s.getSeatNumber()))){
                available = false;
            }

            this.setDisable(!available);
            updateText();

        }


        /**
         * enables, or disables the SeatButton, depending on availability of all seatOptions
         * @param selectedOptions List of all selected FlightOptions
         */
        void optionFilter(List<SeatOption> selectedOptions){
            if(!s.getSeatOptions().containsAll(selectedOptions) || available == false){
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
        for(FlightOptionSelector fos : flightOptionSelectors){


                System.out.println("Adding FlightOption: "+ fos.getFlightOption());
                int qty = fos.getSelected();
                selectedFlightOptions.put(fos.getFlightOption(), qty);

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
        invalidEmailLabel = new Label("The entered Email is invalid. Verify your input or try anothe Email");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("E-Mail for Contacting: "), contactEmailField);
        paxListVBox.getChildren().add(hBox);
        paxListVBox.getChildren().add(invalidEmailLabel);
    }

    /**
     * checks if all required information of the Passengers is given
     * If all info is given, the next Step(next Tab) is enabled.
     * Else next Step(next Tab) is disabled
     */
    public void checkPaxInfoStatus(){
        if(paxInfoBoxes != null && !paxInfoBoxes.isEmpty() && contactEmailField != null){
            personNameSeatComb = new HashMap<>();
            boolean infoMissing = false;
            for(PaxInfoBox p : paxInfoBoxes){
                if(p.getFirstName().equals("") || p.getLastName().equals("")){
                    infoMissing = true;
                }else{
                    personNameSeatComb.put(p.getSeat(), new Pair<String,String>(p.getFirstName(), p.getLastName()));
                }
            }


            String rfc5322CompliantRegex = "(?:[a-z0-9!#$%&'*+=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
            if(contactEmailField.getText().matches(rfc5322CompliantRegex)){
                invalidEmailLabel.setVisible(false);
            }else{
                invalidEmailLabel.setVisible(true);
                infoMissing = true;
            }
            if(infoMissing) {
                flightOptionsTab.setDisable(true);
            }else
                flightOptionsTab.setDisable(false);
                 contactEmail = contactEmailField.getText();
        }
    }

    /**
     * Collects all Information entered and displays them in textual form
     */
    public void generateOverview(){

        flightOverview.getChildren().add(new Label("FlightNo: "+selectedFlight.getFlightNumber()+"; From: "+selectedFlight.getRoute().getDepartureAirport().toString()+"; To: "+selectedFlight.getRoute().getArrivalAirport().toString()+"; On: "+selectedFlight.getDeparture().toString()));

        double basePricePerSeat = selectedFlight.getPriceWithPriceReductionsApplied();
        contactEmailOverview.getChildren().add(new Label(contactEmail));

        VBox flightOptions = new VBox();
        for(FlightOption fo : selectedFlightOptions.keySet()){
            int qty = selectedFlightOptions.get(fo);
            double pricePerFO = fo.getPrice();
            String overviewString = qty+"x : "+fo.getName()+ " : +"+pricePerFO;
            double flightOptionTotal = qty * pricePerFO;
            sum += flightOptionTotal;
            flightOptions.getChildren().add(new Label(overviewString));
        }
        flightOptionsOverview.getChildren().add(flightOptions);

        //seatsOverviewVBox
        //selectedSeatsForBooking
        seatsOverviewVBox.getChildren().add(new Label("Baseprice per Seat: "+ basePricePerSeat));
        for(Seat s : selectedSeatsForBooking.keySet()){
            HBox seatBox = new HBox();
            Pair<String,String> namePair = personNameSeatComb.get(s);
            String seatInfoString = seatToText(s)+ ": "+ namePair.getValue()+", "+namePair.getKey()+": ";
            seatBox.getChildren().add(new Label(seatInfoString));

            VBox seatOptionsBox = new VBox();
            double seatTotal = basePricePerSeat;

            var seatOptions = selectedSeatsForBooking.get(s);
            for(SeatOption so : seatOptions){
                double seatOptionBasePrice = so.getPrice();
                seatOptionsBox.getChildren().add(new Label(so.getName()+": +"+seatOptionBasePrice+"€"));
                seatTotal += seatOptionBasePrice;
            }
            sum += seatTotal;
            seatBox.getChildren().add(seatOptionsBox);
            seatsOverviewVBox.getChildren().add(seatBox);
        }
        totalPriceVBox.getChildren().add(new Label(sum+"€"));

    }

    @FXML
    void createBooking(ActionEvent event) throws IOException {
        BookingManager bm = App.businessLogicAPI.getBookingManager();
        TicketManager tm = App.businessLogicAPI.getTicketManager();
        List<FlightOption> flightOptions = new ArrayList<FlightOption>();

        for(FlightOption fo : selectedFlightOptions.keySet()){
            String overviewString = selectedFlightOptions.get(fo)+"x : "+fo.getName()+ " : +"+fo.getPrice();
            for(int i = 0; i < selectedFlightOptions.get(fo); i++){
                flightOptions.add(fo);
            }
        }


        List<Ticket> tickets = new ArrayList<>();

        for(Seat s : selectedSeatsForBooking.keySet()){
            Pair<String,String> namePair = personNameSeatComb.get(s);
            SeatOption[] seatOptions = selectedSeatsForBooking.get(s).toArray(SeatOption[]::new);
            Ticket t = tm.createTicket(this.selectedFlight, s, namePair.getKey(), namePair.getValue(), seatOptions);
            tickets.add(t);
        }

        Ticket[] ticketsForBooking = tickets.toArray(Ticket[]::new);
        Booking booking = bm.createBooking((SalesEmployee) App.employee, this.selectedFlight, ticketsForBooking, flightOptions.toArray(FlightOption[]::new), contactEmail, LocalDateTime.now(), sum);

        boolean saveComplete = true;
        if(!App.businessLogicAPI.addBookingFromUI(booking)){
            saveComplete = false;
        }

        if(!saveComplete){
            System.out.println("Speichern des Booking fehlgeschlagen!");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving booking");
            alert.setContentText("There was an error while saving the created booking. Try again!");
            alert.showAndWait();

            App.setRoot("salesEmployeeHome");

        }else{
            System.out.println("Speichern des Booking erfolgreich");
            App.setRoot("salesEmployeeHome");
        }



    }

    /**
     *
     * @param seat
     * @return SeatPosition in format, which is used in the real world (e.g. 5F, 6D) not Row:5 Col:6
     */
     static String seatToText(Seat seat) {
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

    /**
     * A Hbox containing the SeatNumber and two TextFields, that must be filled with the First and LastName of the passenger
     * taking up this seat
     */
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
            this.getChildren().addAll(new Label("Passenger for Seat "+ seatToText(seat)), firstName, lastName);
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


    }

}
