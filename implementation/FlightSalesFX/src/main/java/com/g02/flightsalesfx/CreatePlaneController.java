package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Plane;
import com.g02.flightsalesfx.businessEntities.Seat;
import com.g02.flightsalesfx.persistence.PlaneStorageService;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatePlaneController {

    private final ToggleGroup toggleGroupSeatOptions = new ToggleGroup();

    /*    @FXML
        public VBox flightOptions;*/
    private final List<Seat> seats = new ArrayList<>();
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
    private SeatOption currentSelected = null;

    @FXML
    private void addSeatRow() {
        System.out.println("add row");
        seatContainer.getChildren().add(createRow());
    }

    @FXML
    private void cloneSeatRow() {
        System.out.println("Clone row");
        var row = createRow();
        if (!seatContainer.getChildren().isEmpty()) {
            var node = ((VBox) seatContainer.getChildren().get(seatContainer.getChildren().size() - 1)).getChildren().size() - 1;
            for (int i = 0; i < node; i++) {
                var seat = new Seat(row);
                seats.add(seat);
                row.getChildren().add(row.getChildren().size() - 1, seat);
            }
        }
        seatContainer.getChildren().add(row);
        updateSeatText();
    }

    @FXML
    private void addSeatOption() {
        seatOptions.getChildren().add(seatOptions.getChildren().size() - 1, new SeatOption());
    }

    private VBox createRow() {
        var box = new VBox();
        box.setSpacing(2);
        box.setAlignment(Pos.CENTER);
        var addButton = new Button();
        addButton.setText("ADD");
        addButton.setFont(Font.font("Source Code Pro Semibold"));
        addButton.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        addButton.setOnAction(actionEvent -> {
            var seatButton = new Seat(box);
            this.seats.add(seatButton);
            var children = box.getChildren();
            children.add(children.size() - 1, seatButton);
            updateSeatText();
        });
        addButton.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                System.out.println("right click");
                box.getChildren().forEach(o -> {
                    assert o instanceof Seat;
                    seats.remove(o);
                });
                seatContainer.getChildren().remove(box);
            }
        });
        box.getChildren().add(addButton);
        return box;
    }

    @FXML
    private void savePlane() {
        var name = planeName.getText();
        var type = planeType.getText();
        var manufacturer = planeManufacturer.getText();
        var plane = App.businessLogicAPI.getPlaneManager().createPlane(name, manufacturer, type);
        var seatsStream = seats.stream().map(seat -> App.businessLogicAPI.getSeatManager().createSeat(seat.row(), seat.column(), seat.options.stream()
                .map(seatOption -> App.businessLogicAPI.getOptionManager().createSeatOption(seatOption.getSeatOptionName()))
                .collect(Collectors.toList())));
        plane.addAllSeats(seatsStream.sorted().collect(Collectors.toList()));
        System.out.println(plane);
        var planeStorageService = App.persistenceAPI.getPlaneStorageService(App.businessLogicAPI.getPlaneManager());
        planeStorageService.add(plane);
        exit();
    }

    @FXML
    private void exit(){
        try {
            App.setRoot("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSeatText() {
        seats.forEach(Seat::updateText);
    }

    private class SeatOption extends HBox {
        String optionName = "";
        ToggleButton chooseButton;
        TextField changeNameTextField;
        Spinner<Double> changeAvailableSpinner;

        public SeatOption() {
            chooseButton = new ToggleButton();
            changeNameTextField = new TextField();
            changeAvailableSpinner = new Spinner<>();
            changeAvailableSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, Double.MAX_VALUE, 0, 0.01));
            changeAvailableSpinner.setEditable(true);
            this.setSpacing(10);
            this.getChildren().addAll(chooseButton, changeNameTextField, changeAvailableSpinner);
            this.changeNameTextField.textProperty().addListener(e -> {
                optionName = changeNameTextField.getText();
            });
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

        public String getSeatOptionName() {
            return changeNameTextField.getText();
        }
    }

    private class Seat extends Button {
        List<SeatOption> options = new ArrayList<>();
        private VBox box;

        public Seat(VBox box) {
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

        public int row() {
            return seatContainer.getChildren().indexOf(box);
        }

        public int column() {
            return box.getChildren().indexOf(this);
        }
    }

}
