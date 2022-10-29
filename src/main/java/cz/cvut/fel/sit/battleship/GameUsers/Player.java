package cz.cvut.fel.sit.battleship.GameUsers;

import cz.cvut.fel.sit.battleship.GameConnection.Client;
import cz.cvut.fel.sit.battleship.GameField.Board;
import cz.cvut.fel.sit.battleship.GameField.Square;
import cz.cvut.fel.sit.battleship.GameField.SquareStatus;
import cz.cvut.fel.sit.battleship.ShipTypes.*;

import java.util.ArrayList;


public class Player {
    public String name;
    private int strikes = 0;
    private int misses = 0;
    private Client client;
    private boolean visible;
    public Board field = new Board();
    public Ship[] playersShips;
    public boolean clickable = true;
    public boolean readyToPlay = false;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getField() {
        return field;
    }

    public void setField(Board field) {
        this.field = field;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Player(String name, Ship[] playersShips, boolean visible, int size) {
        this.name = name;
        this.playersShips = playersShips;
        this.visible = visible;
        this.field.boardConstruction(this.visible, size);
    }

    public boolean isPlayer() {
        return this.name == "YOU";
    }

    public void isFiredSquare(Board board, int x, int y) throws RepeatedStrikeException {
        if ((board.getSquare(x, y).getStatus() == SquareStatus.Miss) || (board.getSquare(x, y).getStatus() == SquareStatus.BombedShip)) {
            throw new RepeatedStrikeException();
        }
    }

    //Returns result of fire
    public SquareStatus fire(Player enemy, int[] position) {
        int x = position[0];
        int y = position[1];
            Square square = enemy.getField().getSquare(x, y);
            if (square.getStatus() == SquareStatus.Ship) {
                square.setStatus(SquareStatus.BombedShip);
                strikes++;
                enemy.getBoatByCoordinates(x, y).isAliveCheck();
                return SquareStatus.BombedShip;
            } else if ((square.getStatus() == SquareStatus.Water) || (square.getStatus() == SquareStatus.TooClose)) {
                square.setStatus(SquareStatus.Miss);
                misses++;
                return SquareStatus.Miss;
            }
            return square.getStatus();
    }

    public String shipsToString() {
        var res = "";
        for (Ship ship : playersShips) {
            res += ship.getFstSquare() + "-";
            res += ship.getShipOrientation();
            res += ";";
        }
        return res;
    }

    public Ship getBoatByCoordinates(int x, int y) {
        for (Ship ship : playersShips) {
            if (ship.isSquareIncluded(x, y)) {
                return ship;
            }
        }
        return null;
    }

    public boolean allShipsSunk() {
        for (Ship ship : playersShips) {
            if (ship.isAliveState()) {
                return false;
            }
        }
        return true;
    }

    public void setupShipsFromString(String actionPayload) {
        var shipStr = actionPayload.split(";");
        int i = 0;
        for (String ship  : shipStr) {
            var pos = ship.split("-")[0];
            var orientation = ship.split("-")[1];
            var playerShip = playersShips[i++];
            if (playerShip.getFstSquare() != pos){
                var cord = pos.split(",");
                var cordArr = new int[cord.length];
                for (int j = 0; j < cord.length; j++) {
                    cordArr[j] = Integer.parseInt(cord[j]);
                }
                playerShip.setBoard(field);
                playerShip.setFstSquare(field.getSquare(cordArr[0], cordArr[1]));
                playerShip.setOrientation(Orientation.valueOf(orientation));
                playerShip.placeShip();
            }
        }
        readyToPlay = true;
    }
}
