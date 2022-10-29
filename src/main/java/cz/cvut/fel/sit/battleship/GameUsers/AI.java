package cz.cvut.fel.sit.battleship.GameUsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import cz.cvut.fel.sit.battleship.GameField.*;
import cz.cvut.fel.sit.battleship.ShipTypes.*;

public class AI {
    private final Player local;
    private final Board enemyBoard;

    public AI(Player local, Board enemyBoard) {
        this.local = local;
        this.enemyBoard = enemyBoard;
    }

    public boolean isValid(Square chosenSquare) {
        return chosenSquare.getStatus() == SquareStatus.Water || chosenSquare.getStatus() == SquareStatus.Ship || chosenSquare.getStatus() == SquareStatus.TooClose;
    }

    public void placeAllShips(Ship[] aiShips) {
        int[] target = new int[2];
        int orient;
        Orientation orientation;

        for (int i = 0; i < aiShips.length; i++)
            do {
                //set Board
                aiShips[i].board = local.field;
                aiShips[i].setAliveState(true);
                //set Random Orientation
                Random r = new Random();
                orient = r.nextInt(2);
                orientation = (orient == 0) ? Orientation.horizontal : Orientation.vertical;

                aiShips[i].setOrientation(orientation);
                if (orientation == Orientation.vertical) {
                    target[0] = r.nextInt(local.field.sideSize);
                    target[1] = checkPosition(aiShips[i].getSize(), local.field.sideSize);
                } else {
                    target[0] = checkPosition(aiShips[i].getSize(), local.field.sideSize);
                    target[1] = r.nextInt(local.field.sideSize);
                }
                //set Random Position
                aiShips[i].setFstSquare(local.field.getSquare(target[0], target[1]));


            } while (!aiShips[i].placeShip());
        local.readyToPlay = true;
    }

    private int checkPosition(int size, int boardSize) {
        Random r = new Random();
        return r.nextInt(boardSize - size);
    }

    public void fireAI(Player enemy) {
        ArrayList<Square> availableSquares = new ArrayList<>();
        for (int i = 0; i < enemyBoard.sideSize; i++) {
            for (int j = 0; j < enemyBoard.sideSize; j++) {
                Square square = enemyBoard.getSquare(i, j);
                if (isValid(square)) availableSquares.add(square);
            }
        }
        while (!availableSquares.isEmpty()) {
            Square res = shootRandomSquare(availableSquares, enemy);
            availableSquares.remove(res);
            if (res.getStatus() == SquareStatus.Miss)
                return;
            else if (res.getStatus() == SquareStatus.BombedShip) {
                if (!tryToSinkShip(res, enemy))
                    return;
            }
        }
    }

    private boolean tryToSinkShip(Square source, Player enemy) {
        List<Square> available = source.getCloseAdjacentTiles(enemyBoard).stream().filter(this::isValid).collect(Collectors.toList());

        if (available.size() == 0) return true;

        Square res = shootRandomSquare(available, enemy);
        if (res.getStatus() == SquareStatus.BombedShip) {
            return tryToSinkShip(res, enemy);
        }
        return false;
    }

    private Square shootRandomSquare(List<Square> list, Player enemy) {
        Random r = new Random();
        Square square;

        if (list.size() == 1)
            square = list.get(0);
        else
            square = list.get(r.nextInt(list.size() - 1));

        int[] pos = new int[2];
        pos[0] = square.getX();
        pos[1] = square.getY();

        local.fire(enemy, pos);
        return square;
    }
}