package cz.cvut.fel.sit.battleship.GameInterface.UIFactory;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UIButtonFactory {
    public static Button createButton(String title, Color textColor, Background background) {
        Button button = new Button(title);
        button.setFont(Font.font("Comic Sans MS", 15));
        button.setTextFill(textColor);
        button.setBackground(background);

        return button;
    }
}
