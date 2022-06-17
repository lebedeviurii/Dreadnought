package cz.cvut.fel.sit.battleship;

import cz.cvut.fel.sit.battleship.GameConfiguration.GameMode;
import cz.cvut.fel.sit.battleship.GameConfiguration.Mode;
import cz.cvut.fel.sit.battleship.GameConnection.GameState;
import cz.cvut.fel.sit.battleship.GameInterface.model.GameViewManager;
import cz.cvut.fel.sit.battleship.GameUsers.AI;
import cz.cvut.fel.sit.battleship.GameUsers.Player;
import cz.cvut.fel.sit.battleship.ShipTypes.Orientation;
import cz.cvut.fel.sit.battleship.ShipTypes.Ship;

import java.io.IOException;

public class GameLogic {
    private final GameViewManager gameView;
    private int size;

    public GameLogic(GameViewManager gameViewManager) {
        this.gameView = gameViewManager;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getLocalEnemy() {
        return localEnemy;
    }

    private Player player;
    private Player localEnemy;

    private Player enemy;
    private AI ai;

    public Ship[] playersShips;
    public Ship[] aiShips;
    public Ship[] enemyShips;

    private GameState gameState;

    public void localStart(String mode) throws Exception {
        GameMode gamemode = new GameMode();
        gamemode.getPropValues(mode);

        size = gamemode.getSize();
        playersShips = cloneShips(gamemode.ships);
        aiShips = cloneShips(gamemode.ships);

        player = new Player("YOU", playersShips, true, gamemode.size);

        localEnemy = new Player("OPPONENT", aiShips, false, gamemode.size);

        ai = new AI(localEnemy, player.field);

        try {
            ai.placeAllShips(aiShips);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        changeState(GameState.SETUP);
    }

    private Ship[] cloneShips(Ship[] ships) {
        Ship[] res = new Ship[ships.length];
        for (int i = 0; i < ships.length; i++) {
            res[i] = (Ship) ships[i].clone();
        }
        return res;
    }

    public Player getEnemy() {
        return enemy;
    }

    public AI getAi() {
        return ai;
    }

    public boolean playerPlaceShip(Player player, Ship ship, int[] coordinates) {
        ship.setBoard(player.getField());
        ship.setAliveState(true);
        var orientation = player.getField().getCurrentOrientationHorizontal() ?
                Orientation.horizontal : Orientation.vertical;
        ship.setOrientation(orientation);
        ship.setFstSquare(ship.board.getPosition(coordinates[0], coordinates[1]));

        return ship.placeShip();
    }

    public void playerAIFire(Player player, int[] position) {
        switch (player.fire(localEnemy.field, localEnemy, position)) {
            case BombedShip:
                if (localEnemy.field.allShipsSunk())
                    changeState(GameState.FINISH);
            case None:
                return;
        }

        changeState(GameState.PLAY_2);

        ai.fireAI(player);
        if (player.field.allShipsSunk()) {
            changeState(GameState.FINISH);
            return;
        }
        changeState(GameState.PLAY_1);
    }

    public void changeState(GameState gameState){
        switch (gameState) {
            case PLAY_1 -> {
                gameView.startGame();
                gameView.refreshBoards();
            }
            case PLAY_2 -> gameView.refreshBoards();
            case FINISH -> gameView.finishGame();
        }

        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void networkStart() throws IOException {
        GameMode gamemode = new GameMode();
        gamemode.getPropValues(String.valueOf(Mode.standard));

        size = gamemode.getSize();
        playersShips = cloneShips(gamemode.ships);
        enemyShips = cloneShips(gamemode.ships);
        player = new Player("YOU", playersShips, true, gamemode.size);
        enemy = new Player("OPPONENT", enemyShips, false, gamemode.size);

        changeState(GameState.CONNECTION);
    }
}
