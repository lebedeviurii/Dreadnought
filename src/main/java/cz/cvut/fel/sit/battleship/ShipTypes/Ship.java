package cz.cvut.fel.sit.battleship.ShipTypes;

import cz.cvut.fel.sit.battleship.GameField.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Ship {

    public Board board;
    private int size;
    private List<Square> shipPosition = new ArrayList<Square>();
    private Square fstSquare;
    public boolean isAliveState;
    public Orientation shipOrientation;

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Square> getShipPosition() {
        return shipPosition;
    }

    public void setFstSquare(Square fstSquare) {
        this.fstSquare = fstSquare;
    }

    public void setAliveState(boolean aliveState) {
        isAliveState = aliveState;
    }

    public void setOrientation(Orientation orientation) {
        this.shipOrientation = orientation;
    }

    public int getSize() {
        return size;
    }

    public Square getFstSquare() {
        return fstSquare;
    }

    public boolean isAliveState() {
        return isAliveState;
    }

    public Orientation getShipOrientation() {
        return shipOrientation;
    }


    //Ship Placing
    public boolean placeShip(){
        Board board = this.board;
        if (shipOrientation == null){
            if (board.getCurrentOrientationHorizontal()){
                this.setOrientation(Orientation.horizontal);
            } else {
                this.setOrientation(Orientation.vertical);
            }
        }
        if (board.getPositionStatus(fstSquare.getX(), fstSquare.getY()) == SquareStatus.Water){
            return checkAndPlaceShip(this.board, this.shipPosition, this.fstSquare, this.size, this.shipOrientation);
        }
        return false;
    }

    private boolean checkAndPlaceShip(Board board, List<Square> shipPosition, Square fstSquare, int size, Orientation orientation){
        ArrayList<Square> squares = getShipSquares(board, fstSquare, orientation, size);
        if (squares != null){
            for (Square item: squares) {
                item.setStatus(SquareStatus.Ship);
                shipPosition.add(item);
                ArrayList<Square> nearest = adjacentSquare(board, item);
                shipPosition.addAll(nearest);

                this.shipPosition = shipPosition.stream().distinct().collect(Collectors.toList());
            }
            return true;
        }
        return false;
    }

    private ArrayList<Square> adjacentSquare(Board board, Square square){
        ArrayList <Square> res = square.getAdjacentTiles(board);
        for (Square element: res) {
            if (element.getStatus() == SquareStatus.Water){
                element.setStatus(SquareStatus.TooClose);
            }
        }
        return res;
    }

    @Override
    public Object clone() {
        return null;
    }

    private ArrayList<Square> getShipSquares(Board board, Square fstSquare, Orientation orientation, int size){
        ArrayList<Square> res = new ArrayList<Square>();
        switch (orientation){
            case horizontal:
                for (int i = 0; i < size; i++) {
                    Square square = board.getPosition(fstSquare.getX() + i, fstSquare.getY());
                    if (square != null && square.getStatus() == SquareStatus.Water){
                        res.add(square);
                    } else {
                      return null;
                    }
                }
                return res;
            case vertical:
                for (int i = 0; i < size; i++) {
                    Square square = board.getPosition(fstSquare.getX(), fstSquare.getY() + i);
                    if (square != null && square.getStatus() == SquareStatus.Water){
                        res.add(square);
                    } else {
                        return null;
                    }
                }
                return res;
        }
        return null;
    }


    public boolean isSquareIncluded(int x, int y) {
        for (Square square : shipPosition) {
            if (board.getPosition(x, y) == square) {
                return true;
            }
        }
        return false;
    }

    public void isAliveCheck() {
        for (Square square : shipPosition) {
            if (square.getStatus() == SquareStatus.Ship) {
                return;
            }
        }
        isAliveState = false;
        deadShip();
    }

    private void deadShip() {
        for (Square square : shipPosition) {
            if (square.getStatus() == SquareStatus.TooClose) {
                square.setStatus(SquareStatus.Miss);
            }
        }
    }

    public void isOverlap(Board board) throws OverlapException {
        try {
            int startX = fstSquare.getX();
            int startY = fstSquare.getY();

            if (shipOrientation == Orientation.horizontal) {
                if (board.getSideSize() > startX + size) {
                    for (int i = 0; i < size; i++)
                        if (board.getPosition(startX + i, startY).getStatus() == SquareStatus.Ship || board.getPosition(startX + i, startY).getStatus() == SquareStatus.TooClose) {
                            throw new OverlapException();
                        }
                }
            } else {
                if (board.getSideSize() > startY + size) {
                    for (int i = 0; i < size; i++)
                        if (board.getPosition(startX, startY + i).getStatus() == SquareStatus.Ship || board.getPosition(startX, startY + i).getStatus() == SquareStatus.TooClose) {
                            throw new OverlapException();
                        }
                }
            }
        } catch (OverlapException overlapException) {

        }

    }

    public void isAvailable() throws UnableException {
        try {
            if (shipOrientation == Orientation.horizontal) {
                if (fstSquare.getY() < 0 || fstSquare.getY() > board.getSideSize())
                    throw new UnableException();
                if (fstSquare.getX() < 0 || fstSquare.getX() + size - 1 > board.getSideSize())
                    throw new UnableException();
            } else {
                if (fstSquare.getX() < 0 || fstSquare.getX() > board.getSideSize())
                    throw new UnableException();
                if (fstSquare.getY() < 0 || fstSquare.getY() + size - 1 > board.getSideSize())
                    throw new UnableException();
            }
        } catch(UnableException unableException) {

        }
    }
}
