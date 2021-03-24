package com.g02.flightsalesfx;

import com.g02.flightsalesfx.persistence.EmployeeStorageService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class CreatePlaneController {

    @FXML
    public VBox seatOptions;

/*    @FXML
    public VBox flightOptions;*/

    @FXML
    public Button addSeatOptionButton;

    @FXML
    public Button addRow;

    @FXML
    public HBox seatContainer;

    private ToggleGroup toggleGroupSeatOptions = new ToggleGroup();

    private final List<Seat> seats = new ArrayList<>();
    private SeatOption currentSelected = null;

    @FXML
    private void addSeatRow() {
        System.out.println("add row");
        seatContainer.getChildren().add(createRow());
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
        addButton.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        addButton.setOnAction(actionEvent -> {
            var seatButton = new Seat(box);
            this.seats.add(seatButton);
            var children = box.getChildren();
            children.add(children.size() - 1, seatButton);
            seats.forEach(Seat::updateText);
        });
        box.getChildren().add(addButton);
        return box;
    }

    private class SeatOption extends HBox {
        String optionName = "";
        ToggleButton chooseButton;
        TextField changeNameTextField;
        Spinner<Integer> changeAvailableSpinner;

        public SeatOption() {
            chooseButton = new ToggleButton();
            changeNameTextField = new TextField();
            changeAvailableSpinner = new Spinner<>();
            changeAvailableSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
            this.setSpacing(10);
            this.getChildren().addAll(chooseButton, changeNameTextField, changeAvailableSpinner);
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
    }

    private void savePlane() {

    }

    private void updateSeatText() {
        seats.forEach(Seat::updateText);
    }

    private class Seat extends Button {
        List<SeatOption> options = new ArrayList<>();
        private VBox box;

        public Seat(VBox box) {
            this.box = box;
            setOnAction(actionEvent -> {
                if (currentSelected != null) {
                    options.add(currentSelected);
                } else {
                    box.getChildren().remove(this);
                    if (box.getChildren().size() <= 1) {
                        seatContainer.getChildren().remove(box);
                    }
                }
                updateSeatText();
            });
        }

        void updateText() {
            var i = box.getChildren().indexOf(this) + 1;
            var i1 = seatContainer.getChildren().indexOf(box);
            String s;
            if (i1 < 26) {
                s = String.valueOf((char) (i1 + 65));
            } else {
                s = String.valueOf((char) ((i1 / 26) + 64));
                s += String.valueOf((char) (i1 + 65 - 26));
            }
            s += i;
            if (currentSelected != null && options.contains(currentSelected)) {
                s += ": X";
            }
            this.setText(s);
        }
    }

}
