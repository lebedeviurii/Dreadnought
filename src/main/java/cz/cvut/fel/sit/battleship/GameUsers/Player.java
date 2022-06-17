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

    public boolean isEnemy() {
        return this.name == "OPPONENT";
    }

    public void isFiredSquare(Board board, int x, int y) throws RepeatedStrikeException {
        if ((board.getPosition(x, y).getStatus() == SquareStatus.Miss) || (board.getPosition(x, y).getStatus() == SquareStatus.BombedShip)) {
            throw new RepeatedStrikeException();
        }
    }

    //Returns result of fire
    public SquareStatus fire(Board board, Player enemy, int[] position) {
        int x = position[0];
        int y = position[1];
        try {
            isFiredSquare(board, x, y);
            Square square = board.getPosition(x, y);
            if (square.getStatus() == SquareStatus.Ship) {
                square.setStatus(SquareStatus.BombedShip);
                strikes++;
                getBoatByCoordinates(x, y, enemy).isAliveCheck();
                return SquareStatus.BombedShip;
            } else if ((square.getStatus() == SquareStatus.Water) || (square.getStatus() == SquareStatus.TooClose)) {
                square.setStatus(SquareStatus.Miss);
                misses++;
                return SquareStatus.Miss;
            }
            return SquareStatus.Miss;
        } catch (Exception e) {
            return SquareStatus.None;
        }
    }

    public String shipsToString() {
        var res = "";
        for (Ship ship : playersShips) {
            res += ship.getSize() + "-";
            for (Square square : ship.getShipPosition()) {
                res += square.squareToString();
            }
            res += ";";
        }
        return res;
    }

    public Ship getBoatByCoordinates(int x, int y, Player enemy) {
        for (Ship ship : enemy.playersShips) {
            if (ship.isSquareIncluded(x, y)) {
                return ship;
            }
        }
        return null;
    }

    //Statistic for player's motivation :D
    public String getStatsString() {
        return ("Player:" + name + "\nstrikes: " + strikes + "\nmisses: " + misses);
    }

    public void setupShipsFromString(String actionPayload) {
        var shipStr = actionPayload.split(";");
        var ships = new ArrayList<Ship>();
        for (String str: shipStr) {
            var size = str.split("-");
            var pos = size[1].split(":");
            Ship ship = null;
            switch (Integer.valueOf(size[0])){
                case 1:
                    ship = new Convoy();
                    break;
                case 2:
                    ship = new Destroyer();
                    break;
                case 3:
                    ship = new Cruiser();
                    break;
                case 4:
                    ship = new Battleship();
                    break;
                case 5:
                    ship = new AircraftCarrier();
                    break;
            }
            for (String square : pos) {
                var tile = square.split(",");
                var boardSquare = field.getPosition(Integer.valueOf(tile[0]), Integer.valueOf(tile[1]));
                boardSquare.setStatus(SquareStatus.valueOf(tile[2]));
                Square shipSquare = boardSquare;
                ship.getShipPosition().add(shipSquare);
            }
            ships.add(ship);
        }
    }
}
