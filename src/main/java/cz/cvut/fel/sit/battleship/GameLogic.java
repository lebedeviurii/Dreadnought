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

    public GameLogic(GameViewManager gameViewManager) {
        this.gameView = gameViewManager;
    }

    public Player getPlayer() {
        return player;
    }

    public GameViewManager getGameView() {
        return gameView;
    }

    private Player player;

    private Player enemy;
    private AI ai;

    public Ship[] playersShips;
    public Ship[] enemyShips;

    private GameState gameState;

    public void localStart(String mode) throws Exception {
        GameMode gamemode = new GameMode();
        gamemode.getPropValues(mode);

        playersShips = cloneShips(gamemode.ships);
        enemyShips = cloneShips(gamemode.ships);

        player = new Player("YOU", playersShips, true, gamemode.size);

        enemy = new Player("OPPONENT", enemyShips, false, gamemode.size);

        ai = new AI(enemy, player.field);

        try {
            ai.placeAllShips(enemyShips);
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
        ship.setFstSquare(ship.board.getSquare(coordinates[0], coordinates[1]));

        return ship.placeShip();
    }

    public void playerAIFire(int[] position) {
        switch (this.player.fire(enemy, position)) {
            case BombedShip:
                if (enemy.allShipsSunk())
                    changeState(GameState.FINISH);
            case None:
                return;
        }
        changeState(GameState.PLAY_2);
        ai.fireAI(this.player);
        if (player.allShipsSunk()) {
            changeState(GameState.FINISH);
            return;
        }
        changeState(GameState.PLAY_1);
    }

    public void changeState(GameState gameState){
        switch (gameState) {
            case PLAY_1 -> {
                switch (this.gameState){
                    case INTRO, CONNECTION, SETUP -> gameView.startGame();
                }
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

        playersShips = cloneShips(gamemode.ships);
        enemyShips = cloneShips(gamemode.ships);
        player = new Player("YOU", playersShips, true, gamemode.size);
        enemy = new Player("OPPONENT", enemyShips, false, gamemode.size);

        changeState(GameState. CONNECTION);
    }
}
