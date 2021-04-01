package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Seat;
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

public class CreatePlaneController {

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
    private SeatOption currentSelected = null;

    /**
     * Creates a new SeatRow and adds the corresponding VBox to the container
     */
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
                var seat = new SeatButton(row);
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
        addButton.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                System.out.println("right click");
                box.getChildren().forEach(seats::remove);
                seatContainer.getChildren().remove(box);
                updateSeatText();
            } else if (mouseEvent.getButton()==MouseButton.PRIMARY) {
                createSeat(box);
            }
        });
        box.getChildren().add(addButton);
        return box;
    }

    public void createSeat(VBox box) {
        var seatButton = new SeatButton(box);
        this.seats.add(seatButton);
        var children = box.getChildren();
        children.add(children.size() - 1, seatButton);
        updateSeatText();
    }

    @FXML
    private void savePlane() {
        var name = planeName.getText();
        var type = planeType.getText();
        var manufacturer = planeManufacturer.getText();
        var collect = seats.stream().map(seatButton -> (Seat) seatButton).collect(Collectors.toList());
        var planeCreated = App.businessLogicAPI.createPlaneFromUI(name, type, manufacturer, collect);
        if (planeCreated) {
            exit();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving plane");
            alert.setContentText("There was an error while saving the created plane. Try again!");
            alert.showAndWait();
        }
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
        seats.forEach(SeatButton::updateText);
    }

    public class SeatOption extends HBox {
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

        public String getSeatOptionName() {
            return changeNameTextField.getText();
        }


    }

    public class SeatButton extends Button implements Seat {
        List<SeatOption> options = new ArrayList<>();
        private final VBox box;

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

        public List<SeatOption> getOptions() {
            return options;
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

        @Override
        public int getRowNumber() {
            return row();
        }

        @Override
        public int getSeatNumber() {
            return column();
        }

        @Override
        public void addSeatOption(com.g02.flightsalesfx.businessEntities.SeatOption so) {

        }

        @Override
        public void addAllSeatOptions(List<? extends com.g02.flightsalesfx.businessEntities.SeatOption> seatOptionList) {

        }
    }

}
