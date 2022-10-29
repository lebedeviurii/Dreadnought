package cz.cvut.fel.sit.battleship.GameInterface.view;

import cz.cvut.fel.sit.battleship.GameConnection.ClientProtocol;
import cz.cvut.fel.sit.battleship.GameConnection.GameState;
import cz.cvut.fel.sit.battleship.GameField.Square;
import cz.cvut.fel.sit.battleship.GameField.SquareStatus;
import cz.cvut.fel.sit.battleship.GameInterface.util.BoardImagesSingleton;
import cz.cvut.fel.sit.battleship.GameLogic;
import cz.cvut.fel.sit.battleship.GameUsers.Player;
import cz.cvut.fel.sit.battleship.ShipTypes.Ship;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleShipCanvas {

    private final GameLogic logic;
    private final Canvas setupCanvas;
    private int size;
    private Player player;

    public BattleShipCanvas(Canvas setupCanvas, int size, GameLogic logic) {
        this.setupCanvas = setupCanvas;
        this.size = size;
        this.logic = logic;
        init();
    }

    public Canvas getSetupCanvas() {
        return setupCanvas;
    }

    public void init() {
        GraphicsContext g = setupCanvas.getGraphicsContext2D();

        for (int i = 0; i < size + 1; i++) {
            int x = (int) (setupCanvas.getHeight() / size * i);
            int y = (int) (setupCanvas.getWidth() / size * i);

            g.strokeLine(x, 0, x, setupCanvas.getHeight());
            g.strokeLine(0, y, setupCanvas.getWidth(), y);
        }
    }


    public void initializeWithPlayer(Player player, Player enemy) {
        this.player = player;
        this.size = player.field.sideSize;
        drawBoard();

        AtomicInteger playerShipCounter = new AtomicInteger();

        if (player.isPlayer()) {
            setupCanvas.setOnMouseClicked(mouseEvent -> {
                double squareSize = setupCanvas.getWidth() / size;
                final int x = (int) (mouseEvent.getX() / squareSize);
                final int y = (int) (mouseEvent.getY() / squareSize);
                playerClick(player.getField().getSquare(x,y), playerShipCounter);
            });
        } else {
            setupCanvas.setOnMouseClicked(mouseEvent -> {
                double squareSize = setupCanvas.getWidth() / size;
                final int x = (int) (mouseEvent.getX() / squareSize);
                final int y = (int) (mouseEvent.getY() / squareSize);
                enemyClick(enemy.getField().getSquare(x, y));
            });
        }
    }

    public void enemyClick(Square square) {
        var enemy = logic.getEnemy();
        if (logic.getPlayer().clickable) {
            if ((logic.getGameState() == GameState.PLAY_1) || (logic.getGameState() == GameState.PLAY_2)) {
                if (logic.getAi() != null) {
                    logic.playerAIFire(square.getPosition());
                } else {
                    var newStatus = logic.getPlayer().fire(enemy, square.getPosition());
                    if (newStatus == SquareStatus.Miss) {
                        logic.getPlayer().clickable = false;
                    }
                    enemy.field.getSquare(square.getX(), square.getY()).setStatus(newStatus);
                    logic.getPlayer().getClient().sendToServer(ClientProtocol.HIT, square.cordToString());
                }
                if (logic.getGameState() == GameState.PLAY_1) {
                    logic.changeState(GameState.PLAY_2);
                } else if (logic.getGameState() == GameState.PLAY_2){
                    logic.changeState(GameState.PLAY_1);
                }
                if (enemy.allShipsSunk()) {
                    logic.changeState(GameState.FINISH);
                }
            }
            drawBoard();
        }
    }

    public void playerClick(Square square, AtomicInteger playerShipCounter) {
        if (playerShipCounter.get() < player.playersShips.length) {
            Ship ship = player.playersShips[playerShipCounter.get()];
            if (logic.playerPlaceShip(player, ship, square.getPosition())) {
                playerShipCounter.addAndGet(1);
            }
            drawBoard();
            if (playerShipCounter.get() >= player.playersShips.length) {
                logic.getPlayer().readyToPlay = true;
                if (logic.getEnemy().readyToPlay){
                    switch (logic.getGameState()){
                        case CONNECTION, SETUP -> logic.changeState(GameState.PLAY_1);
                    }
                }
                if (logic.getPlayer().getClient() != null) {
                    logic.getPlayer().getClient().sendToServer(ClientProtocol.BOARD, player.shipsToString());
                }
            }
        }
    }

    public void drawBoard() {
        GraphicsContext g = setupCanvas.getGraphicsContext2D();
        for (int i = 0; i < size; i++) {
            int x = (int) (setupCanvas.getHeight() / size * i);
            for (int j = 0; j < size; j++) {
                int y = (int) (setupCanvas.getWidth() / size * j);
                char square = player.field.getSquare(i, j).draw(player.isVisible());
                g.drawImage(BoardImagesSingleton.getInstance().getImageByChar(square), x, y, setupCanvas.getHeight() / size, setupCanvas.getWidth() / size);
            }
        }
    }

}
