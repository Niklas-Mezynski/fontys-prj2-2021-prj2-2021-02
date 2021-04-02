package com.g02.flightsalesfx.gui;

import com.g02.flightsalesfx.businessEntities.Plane;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;

public interface RowClickHandler<E> {

    void click(MouseEvent mouseEvent, TableRow<E> row);

}
