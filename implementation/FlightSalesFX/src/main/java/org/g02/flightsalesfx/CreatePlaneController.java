package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Plane;
import org.g02.flightsalesfx.businessEntities.Seat;
import org.g02.flightsalesfx.businessEntities.SeatOption;
import org.g02.flightsalesfx.businessLogic.PlaneImpl;
import org.g02.flightsalesfx.businessLogic.SeatImpl;
import org.g02.flightsalesfx.helpers.Bundle;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.g02.flightsalesfx.App.setRoot;

public class CreatePlaneController implements Controller {

    private final ToggleGroup toggleGroupSeatOptions = new ToggleGroup();

    private final List<SeatButton> seats = new ArrayList<>();
    @FXML
    public VBox seatOptions;
    @FXML
    public Button addSeatOptionButton;
    @FXML
    public Button addRow;
    @FXML
    public HBox seatContainer;
    @FXML
    public TextField planeName;
    @FXML
    public TextField planeType;
    @FXML
    public TextField planeManufacturer;
    @FXML
    public Button deleteButton;
    private SeatOptionBox currentSelected = null;
    private boolean editMode;
    private PlaneImpl oldPlane;

    /**
     * Creates a new SeatRow and adds the corresponding VBox to the container
     */
    @FXML
    private void addSeatRow() {
        System.out.println("add row");
        seatContainer.getChildren().add(createRow());
    }

    /**
     * Clones the last added row. Only clones the amount of seats not the SeatOptions.
     */
    @FXML
    private void cloneSeatRow() {
        System.out.println("Clone row");
        // First a new row is created
        var row = createRow();
        // If the last row has children, Seats were added to it so the amount of seats is queried.
        if (!seatContainer.getChildren().isEmpty()) {
            var node = ((VBox) seatContainer.getChildren().get(seatContainer.getChildren().size() - 1)).getChildren().size() - 1;
            // Creating the seats and add it to the new row. The last element is always the ADD button so they are inserted on the index size() - 1
            for (int i = 0; i < node; i++) {
                var seat = new SeatButton(row);
                seats.add(seat);
                row.getChildren().add(row.getChildren().size() - 1, seat);
            }
        }
        // In the end the row is added to the container and all seat labels are updated
        seatContainer.getChildren().add(row);
        // and all seat labels are updated
        updateSeatText();
    }

    /**
     * Adds a new SeatOptionBox to the UI.
     */
    @FXML
    private void addSeatOption() {
        seatOptions.getChildren().add(seatOptions.getChildren().size() - 1, new SeatOptionBox());
    }

    /**
     * Creates a new VBox as a container for all seats in a row. Also adds the ADD button at the end of the VBox
     *
     * @return VBox The created VBox that is not yet added to the container
     */
    private VBox createRow() {
        var box = new VBox();
        // Configuring the VBox with correct spacing and alignment
        box.setSpacing(2);
        box.setAlignment(Pos.CENTER);
        // Creating the ADD button and configuring it with the correct label, font and size
        var addButton = new Button();
        addButton.setText("ADD");
        addButton.setFont(Font.font("Source Code Pro Semibold"));
        addButton.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        addButton.setOnAction(actionEvent -> createSeat(box));
        // Adding the click listener to add seats or remove the row depending on which mouse button was pressed
        addButton.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) { // If it was right click remove the row and all seats that is contains
                System.out.println("right click");
                box.getChildren().forEach(seats::remove);
                seatContainer.getChildren().remove(box);
                updateSeatText();
            }
        });
        // add the button to the VBox
        box.getChildren().add(addButton);
        return box;
    }

    /**
     * Creates a new Seats and adds it to the given box.
     *
     * @param box The box to which the seats are to be added
     */
    public void createSeat(VBox box) {
        var seatButton = new SeatButton(box);
//        Font f=Font.loadFont("file:resources/com/g02/flightsalesfx/SourceCodePro-Regular.ttf",45);
//        seatButton.setFont(f);
        this.seats.add(seatButton);
        var children = box.getChildren();
        children.add(children.size() - 1, seatButton);
        updateSeatText();
    }

    /**
     * Saves the plane that the user created in the ui. If there is an error while creating or saving it an alert dialog is shown.
     */
    @FXML
    private void savePlane() throws IOException {
        var name = planeName.getText();
        var type = planeType.getText();
        var manufacturer = planeManufacturer.getText();
        // Map all internal used SeatButtons to Seat
        var collect = seats.stream()
                .map(s->{
                    var ret = new SeatImpl(s.row(),s.column());
                    ret.addAllSeatOptions(s.options);
                    return (Seat) ret;
                })
                .collect(Collectors.toList());
        // Create the plane using the businessLogicAPI
        Plane updatedPlane;
        if (editMode) {
            updatedPlane = App.businessLogicAPI.updatePlane(oldPlane, name, type, manufacturer, collect);
        } else {
            updatedPlane = App.businessLogicAPI.createPlaneFromUI(name, type, manufacturer, collect);
        }

        if (updatedPlane != null) { // If the plane was saved successfully exit and return to the previous scene
            exit();
        } else { // If there was an error, create and show and alert dialog to notify the user of this error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving plane");
            alert.setContentText("There was an error while saving the created plane. Try again!");
            alert.showAndWait();
        }
    }

    /**
     * Exit the current scene and return to the previous one, using the static setRoot method of App
     * @see App#setRoot(String)
     */
    @FXML
    private void exit() throws IOException {
        App.setRoot("home");
    }

    /**
     * If view in edit mode delete the plane that is currently being edited
     */
    @FXML
    private void delete() {
        if (editMode) {
            var plane = App.businessLogicAPI.deletePlane(oldPlane);
            System.out.println(plane);
            if (plane) {
                App.setRoot("home");
            } else {
                System.out.println("Error deleting plane");
            }
        }
    }

    private void updateSeatText() {
        seats.forEach(SeatButton::updateText);
    }

    @Override
    public void init(Bundle bundle) {
        if (bundle.getBoolean("edit", false)) {
            this.editMode = true;
            var plane = bundle.get("plane", PlaneImpl.class);
            this.oldPlane = plane;
            planeName.setText(oldPlane.getName());
            planeType.setText(oldPlane.getType());
            planeManufacturer.setText(oldPlane.getManufacturer());
            System.out.println(plane);
            int lastRowNum = -1;
            VBox lastRow = null;
            for (SeatImpl seat : plane.seatList) {
                if (seat.getRowNumber() > lastRowNum) {
                    lastRowNum++;
                    lastRow = createRow();
                    seatContainer.getChildren().add(lastRow);
                }
                createSeat(lastRow);
            }
        } else {
            deleteButton.setVisible(false);
        }
    }

    /**
     * Inner class that represents a SeatOption on the UI.
     * Extends HBox and has a Button, TextField and Spinner
     */
    public class SeatOptionBox extends HBox implements SeatOption {
        String optionName = "";
        // Button to toggle which SeatOption will be applied to the Seats if they are selected
        ToggleButton chooseButton;
        // TextField to enter the name of the SeatOption
        TextField changeNameTextField;
        // Spinner to enter the price of the SeatOption
        Spinner<Double> changeAvailableSpinner;

        public SeatOptionBox() {
            chooseButton = new ToggleButton();
            changeNameTextField = new TextField();
            changeAvailableSpinner = new Spinner<>();
            changeAvailableSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, Double.MAX_VALUE, 0, 0.01));
            changeAvailableSpinner.setEditable(true);
            this.setSpacing(10);
            this.getChildren().addAll(chooseButton, changeNameTextField, changeAvailableSpinner);
            this.changeNameTextField.textProperty().addListener(e -> optionName = changeNameTextField.getText());
            chooseButton.getStyleClass().add("selectSeatOption");
            chooseButton.setToggleGroup(toggleGroupSeatOptions);
            chooseButton.setOnAction(actionEvent -> {
                if (currentSelected != null && currentSelected.equals(this)) {
                    currentSelected = null;
                    System.out.println("deselect seatOption");
                    updateSeatText();
                    return;
                }
                currentSelected = this;
                updateSeatText();
                System.out.println("select seatOption");
            });
        }

        /**
         * @return The Name of this Option
         */
        @Override
        public String getName() {
            return changeNameTextField.getText();
        }

        /**
         * @return The price that this FlightOption costs
         */
        @Override
        public double getPrice() {
            return changeAvailableSpinner.getValue();
        }

        @Override
        public int getID() {
            return -1;
        }
    }

    /**
     * Inner class SeatButton represents a Seat as a Button on the UI
     */
    public class SeatButton extends Button {
        private final VBox box;
        List<SeatOptionBox> options = new ArrayList<>();

        public SeatButton(VBox box) {
            this.box = box;
            this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            this.setFont(Font.font("Source Code Pro Semibold"));
            setOnAction(actionEvent -> {
                if (currentSelected != null) {
                    if (!options.contains(currentSelected)) {
                        options.add(currentSelected);
                    } else {
                        options.remove(currentSelected);
                    }
                } else {
                    box.getChildren().remove(this);
                    if (box.getChildren().size() <= 1) {
                        seatContainer.getChildren().remove(box);
                    }
                    seats.remove(this);
                }
                updateSeatText();
            });
        }


        /**
         * Updates the text and converts row and seat number to a better readable string consisting of
         */
        void updateText() {
            var i = column();
            var i1 = row() + 1;
            String s = String.format("%02d", i1);
            if (i < 26) {
                s += String.valueOf((char) (i + 65));
            } else {
                s += String.valueOf((char) ((i / 26) + 64));
                s += String.valueOf((char) (i + 65 - 26));
            }
            if (currentSelected != null && options.contains(currentSelected)) {
                //s += ": X";
                this.setStyle("-fx-text-fill: #007698; ");
            } else {
                this.setStyle("");
            }
            this.setText(s);
        }

        /**
         * get the row of the seat
         * @return Integer
         */
        public int row() {
            return seatContainer.getChildren().indexOf(box);
        }

        /**
         * get the seat number of the seat
         * @return Integer
         */
        public int column() {
            return box.getChildren().indexOf(this);
        }

        /**
         * get the row of the seat
         * @return Integer
         */
//        @Override
//        public int getRowNumber() {
//            return row();
//        }
//
//        /**
//         * get the seat number of the seat
//         * @return Integer
//         */
//        @Override
//        public int getSeatNumber() {
//            return column();
//        }
//
//        /**
//         * @param so The SeatOption to add to this Seat
//         */
//        @Override
//        public void addSeatOption(com.g02.flightsalesfx.businessEntities.SeatOption so) { }
//
//        /**
//         * @param seatOptionList The List of SeatOptions to add
//         */
//        @Override
//        public void addAllSeatOptions(List<? extends com.g02.flightsalesfx.businessEntities.SeatOption> seatOptionList) {
//
//        }
//
//        @Override
//        public SeatOptionImpl[] getSeatOptions() {
//            SeatOptionImpl[] seatOptions = this.options.stream()
//                    .map(seatOptionBox -> businessLogicAPI.getOptionManager().createSeatOption(seatOptionBox.getName(), seatOptionBox.getPrice()))
//                    .toArray(SeatOptionImpl[]::new);
//            return seatOptions;
//        }
    }

}
