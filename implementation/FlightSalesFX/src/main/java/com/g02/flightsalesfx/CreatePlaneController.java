package com.g02.flightsalesfx;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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

    private final ToggleGroup toggleGroupSeatOptions = new ToggleGroup();

    private final List<Seat> seats = new ArrayList<>();
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
                    if (o instanceof Seat)
                        seats.remove(o);
                    else
                        assert false;
                });
                seatContainer.getChildren().remove(box);
            }
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
            this.changeNameTextField.textProperty().addListener(e->{
                optionName=changeNameTextField.getText();
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
            this.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            this.setFont(Font.font("Source Code Pro Semibold"));
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
            var i = box.getChildren().indexOf(this);
            var i1 = seatContainer.getChildren().indexOf(box) + 1;
            String s = String.format("%02d", i1);
            if (i < 26) {
                s += String.valueOf((char) (i + 65));
            } else {
                s += String.valueOf((char) ((i / 26) + 64));
                s += String.valueOf((char) (i + 65 - 26));
            }
            if (currentSelected != null && options.contains(currentSelected)) {
                s += ": X";
            }
            this.setText(s);
        }
    }

}
