package cz.cvut.fel.sit.battleship.GameInterface.view;

import cz.cvut.fel.sit.battleship.GameConnection.ClientProtocol;
import cz.cvut.fel.sit.battleship.GameConnection.GameState;
import cz.cvut.fel.sit.battleship.GameInterface.util.BoardImagesSingleton;
import cz.cvut.fel.sit.battleship.GameLogic;
import cz.cvut.fel.sit.battleship.GameUsers.Player;
import cz.cvut.fel.sit.battleship.ShipTypes.Ship;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.concurrent.atomic.AtomicInteger;

public class BattleShipCanvas {
    private final Canvas setupCanvas;
    private int size;
    private Player player;

    public BattleShipCanvas(Canvas setupCanvas, int size) {
        this.setupCanvas = setupCanvas;
        this.size = size;

        init();
    }

    public Canvas getSetupCanvas() {
        return setupCanvas;
    }

    public int getSize() {
        return size;
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


    public void initializeWithPlayer(Player player, Player enemy, GameLogic logic) {
        this.player = player;
        this.size = player.field.sideSize;
        drawBoard();

        AtomicInteger playerShipCounter = new AtomicInteger();
        int[] position = new int[2];

        if (player.isPlayer()) {
            setupCanvas.setOnMouseClicked(mouseEvent -> {
                double squareSize = setupCanvas.getWidth() / size;
                position[0] = (int) (mouseEvent.getX() / squareSize);
                position[1] = (int) (mouseEvent.getY() / squareSize);
                playerClick(position, playerShipCounter, logic);
            });
        } else {
            setupCanvas.setOnMouseClicked(mouseEvent -> {
                double squareSize = setupCanvas.getWidth() / size;
                position[0] = (int) (mouseEvent.getX() / squareSize);
                position[1] = (int) (mouseEvent.getY() / squareSize);
                enemyClick(position, enemy, logic);
            });
        }
    }

    public void enemyClick(int[] position, Player enemy, GameLogic logic) {
        var board = enemy.field;
        if (logic.getGameState() == GameState.PLAY_1) {
            if (logic.getAi() != null) {
                logic.playerAIFire(enemy, position);
            } else {
                var newStatus = logic.getPlayer().fire(board, logic.getPlayer(), position);
                enemy.field.getPosition(position[0], position[1]).setStatus(newStatus);
            }
            drawBoard();
        }

    }

    public void playerClick(int[] position, AtomicInteger playerShipCounter, GameLogic logic) {
        if (playerShipCounter.get() < player.playersShips.length) {
            Ship ship = player.playersShips[playerShipCounter.get()];
            if (logic.playerPlaceShip(player, ship, position)) {
                playerShipCounter.addAndGet(1);
            }
            drawBoard();

            if (playerShipCounter.get() >= player.playersShips.length) {
                if (logic.getGameState() == GameState.SETUP)
                    logic.changeState(GameState.PLAY_1);
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
                char square = player.field.getPosition(i, j).draw(player.isVisible());
                g.drawImage(BoardImagesSingleton.getInstance().getImageByChar(square), x, y, setupCanvas.getHeight() / size, setupCanvas.getWidth() / size);

            }
        }
    }

}
