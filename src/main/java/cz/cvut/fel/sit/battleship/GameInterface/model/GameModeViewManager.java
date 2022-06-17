package cz.cvut.fel.sit.battleship.GameInterface.model;

import cz.cvut.fel.sit.battleship.GameConfiguration.Mode;
import cz.cvut.fel.sit.battleship.GameInterface.UIFactory.UIButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameModeViewManager {
    private static final int H = 768;
    private static final int W = 1024;
    private VBox vBox = new VBox();
    private Scene modeScene = new Scene(vBox, W, H);
    private Stage modeStage;

    private GameViewManager gameViewManager;

    public void initModeChooser(Stage stage, GameViewManager gVM) {
        modeStage = stage;
        gameViewManager = gVM;
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(UIConstants.BASE_BACKGROUND);
        vBox.setSpacing(40);


        Button exitButton = UIButtonFactory.createButton("Back", Color.WHITE, UIConstants.DARK_BUTTON_BACKGROUND);
        exitButton.setOnMouseClicked(mouseEvent -> {
            new ViewManager(stage).getMainStage().show();
        });

        ComboBox comboBox = new ComboBox<>();

        for (Mode mode : Mode.values()) {
            comboBox.getItems().add(mode);
        }

        VBox submitBox = new VBox();
        Button submitButton = UIButtonFactory.createButton("Submit", Color.WHITE, UIConstants.LIGHT_BUTTON_BACKGROUND);
        submitButton.setOnMouseClicked(mouseEvent -> {
            if (comboBox.getValue() != null){
                gameViewManager.createNewLocalGame(modeStage, (Mode) comboBox.getValue());
            }
        });
        submitBox.setAlignment(Pos.CENTER);
        submitBox.setSpacing(20);
        submitBox.getChildren().addAll(submitButton, exitButton);

        vBox.getChildren().addAll(comboBox, submitBox);
        modeStage.setScene(modeScene);
        modeStage.show();
    }


    public VBox getVBox() {
        return vBox;
    }
}
