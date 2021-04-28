package org.g02.flightsalesfx.gui;

import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;

public interface RowClickHandler<E> {

    void click(MouseEvent mouseEvent, TableRow<E> row);

}
