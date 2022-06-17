package cz.cvut.fel.sit.battleship.GameInterface.model;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class UIConstants {
    static final Background BASE_BACKGROUND = new Background(new BackgroundFill(Color.web("#3E7999"), CornerRadii.EMPTY, Insets.EMPTY));
    static final Background DARK_BUTTON_BACKGROUND = new Background(new BackgroundFill(Color.web("#274B60"), new CornerRadii(10), Insets.EMPTY));
    static final Background LIGHT_BUTTON_BACKGROUND = new Background(new BackgroundFill(Color.web("#629EBF"), new CornerRadii(10), Insets.EMPTY));

}
