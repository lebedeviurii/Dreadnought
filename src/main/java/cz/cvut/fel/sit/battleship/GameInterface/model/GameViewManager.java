package cz.cvut.fel.sit.battleship.GameInterface.model;

import cz.cvut.fel.sit.battleship.GameConfiguration.Mode;
import cz.cvut.fel.sit.battleship.GameInterface.UIFactory.UIButtonFactory;
import cz.cvut.fel.sit.battleship.GameInterface.view.BattleShipCanvas;
import cz.cvut.fel.sit.battleship.GameLogic;
import cz.cvut.fel.sit.battleship.NetworkGame;
import cz.cvut.fel.sit.battleship.ShipTypes.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GameViewManager {
    private static final int H = 768;
    private static final int W = 1024;
    private HBox gamePane;
    private Scene gameScene;
    private Stage stage;
    private GameLogic gameLogic = new GameLogic(this);
    private BattleShipCanvas player;
    private BattleShipCanvas enemy;
    private Label playerLabel;
    private Label enemyLabel;

    private Text gameInfo;
    private VBox buttonBox;

    private void initializeStage() {
        gamePane = createBoards();
        gameScene = new Scene(gamePane, W, H);
        stage.setScene(gameScene);
        stage.show();
    }

    public BattleShipCanvas getPlayer() {
        return player;
    }

    public BattleShipCanvas getEnemy() {
        return enemy;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void createNewLocalGame(Stage menuStage, Mode mode) {
        this.stage = menuStage;
        String modeStr = mode.toString();
        initializeStage();
        gamePane.setBackground(UIConstants.BASE_BACKGROUND);
        try {
            gameLogic.localStart(modeStr);

            player.initializeWithPlayer(gameLogic.getPlayer(), gameLogic.getEnemy());
            enemy.initializeWithPlayer(gameLogic.getEnemy(), gameLogic.getPlayer());

            playerLabel.setText(gameLogic.getPlayer().name);
            enemyLabel.setText(gameLogic.getEnemy().name);
            gameInfo.setText("PLACE SHIPS");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        stage.show();
    }


    public HBox createBoards() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(50);

        Button exitButton = UIButtonFactory.createButton("Back", Color.WHITE, UIConstants.DARK_BUTTON_BACKGROUND);
        exitButton.setOnMouseClicked(mouseEvent -> {
            new ViewManager(stage).getMainStage().show();
        });

        VBox playerBox = new VBox();
        playerBox.setAlignment(Pos.CENTER);
        playerLabel = new Label();
        Canvas playerCanvas = new Canvas(300, 300);
        playerBox.getChildren().addAll(playerCanvas, playerLabel);
        player = new BattleShipCanvas(playerCanvas, 10, gameLogic);

        VBox enemyBox = new VBox();
        enemyBox.setAlignment(Pos.CENTER);
        enemyLabel = new Label();
        Canvas enemyCanvas = new Canvas(300, 300);
        enemyBox.getChildren().addAll(enemyCanvas, enemyLabel);
        enemy = new BattleShipCanvas(enemyCanvas, 10, gameLogic);

        VBox infoBox = new VBox();
        infoBox.setAlignment(Pos.CENTER);
        gameInfo = new Text();
        infoBox.setSpacing(40);

        buttonBox = new VBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        Text orientation = new Text();

        Button rotateBtn = UIButtonFactory.createButton("Rotate", Color.WHITE, UIConstants.LIGHT_BUTTON_BACKGROUND);
        rotateBtn.setOnAction(actionEvent -> rotationEvent(orientation));

        buttonBox.getChildren().addAll(rotateBtn, orientation);
        infoBox.getChildren().addAll(gameInfo, buttonBox, exitButton);

        hBox.getChildren().addAll(enemyBox, infoBox, playerBox);

        return hBox;
    }


    private void rotationEvent(Text text) {
        if (gameLogic != null && gameLogic.getPlayer() != null) {
            var userBoard = gameLogic.getPlayer().getField();
            userBoard.setCurrentOrientation(!userBoard.getCurrentOrientationHorizontal());
            text.setText(String.valueOf(userBoard.getCurrentOrientationHorizontal() ?
                    Orientation.horizontal : Orientation.vertical));
        }
    }

    public void startGame() {
        gameInfo.setText("GAME STARTED");
        buttonBox.setVisible(false);
    }

    public void refreshBoards() {
        if (player != null){
            player.drawBoard();
        }
        if (enemy != null){
            enemy.drawBoard();
        }
    }

    public void finishGame() {
        gameLogic.getEnemy().setVisible(true);
        refreshBoards();
        player.getSetupCanvas().setOnMouseClicked(mouseEvent -> {
        });
        enemy.getSetupCanvas().setOnMouseClicked(mouseEvent -> {
        });

        if (gameLogic.getPlayer().allShipsSunk()) {
            gameInfo.setText("YOU LOSE");
        } else {
            gameInfo.setText("YOU WON");
        }
    }

    public void createNetworkGame(Stage mainStage, Mode mode, String name) {
        this.stage = mainStage;
        gameLogic = new GameLogic(this);
        initializeStage();
        gamePane.setBackground(UIConstants.BASE_BACKGROUND);
        try {
            gameLogic.networkStart();
            player.initializeWithPlayer(gameLogic.getPlayer(), gameLogic.getEnemy());
            enemy.initializeWithPlayer(gameLogic.getEnemy(), gameLogic.getPlayer());

            playerLabel.setText(gameLogic.getPlayer().name);
            enemyLabel.setText(gameLogic.getEnemy().name);
            gameInfo.setText("PLACE SHIPS");

        } catch (IOException e){
            e.printStackTrace();
        }
        stage.show();
    }
}
