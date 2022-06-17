package cz.cvut.fel.sit.battleship.GameInterface.model;

import cz.cvut.fel.sit.battleship.GameConfiguration.Mode;
import cz.cvut.fel.sit.battleship.GameInterface.UIFactory.UIButtonFactory;
import cz.cvut.fel.sit.battleship.NetworkGame;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NetworkLogInViewManager {
    private static final int H = 768;
    private static final int W = 1024;
    private VBox vBox = new VBox();
    private Scene nameScene = new Scene(vBox, W, H);
    private Stage nameStage;

    private GameViewManager gameViewManager;

    public void initLogIn(Stage stage, GameViewManager gVM) {
        nameStage = stage;
        gameViewManager = gVM;
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(UIConstants.BASE_BACKGROUND);
        vBox.setSpacing(40);


        Label nameLabel = new Label("Enter your name:");
        var nameField = new TextField();
        nameField.setMaxWidth(410);
        nameField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {

            }
        });
        Button startButton = UIButtonFactory.createButton("Start", Color.WHITE, UIConstants.LIGHT_BUTTON_BACKGROUND);
        startButton.setOnMouseClicked(mouseEvent -> {
            var name = nameField.getText().strip();
            gameViewManager.createNetworkGame(nameStage, Mode.standard, name);
            NetworkGame nG = new NetworkGame();
            nG.action(name, gameViewManager.getGameLogic());
        });
        Button exitButton = UIButtonFactory.createButton("Back", Color.WHITE, UIConstants.DARK_BUTTON_BACKGROUND);
        exitButton.setOnMouseClicked(mouseEvent -> {
            new ViewManager(stage).getMainStage().show();
        });

        vBox.getChildren().addAll(nameLabel, nameField, startButton, exitButton);
        nameStage.setScene(nameScene);
        nameStage.show();
    }


}
