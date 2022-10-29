package cz.cvut.fel.sit.battleship.GameField;

import java.util.ArrayList;

public class Square {

    private SquareStatus status;
    private final int x;
    private final int y;

    // Constructor
    public Square(int x, int y, SquareStatus status) {
        this.status = status;
        this.x = x;
        this.y = y;
    }

    public void setStatus(SquareStatus status) {
        this.status = status;
    }

    public SquareStatus getStatus() {
        return status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getPosition(){
        return new int[]{x, y};
    }

    public String cordToString(){
        return String.format("%d,%d",x,y);
    }
    public String squareToString(){
        return String.format("%d,%d,%s:", x,y,status);
    }

    public ArrayList<Square> getAdjacentTiles(Board board) {

        ArrayList<Square> list = getCloseAdjacentTiles(board);

        int squareX = getX();
        int squareY = getY();

        //left-upper
        if (squareX - 1 >= 0 && squareY - 1 >= 0)
            list.add(board.getSquare(squareX - 1, squareY - 1));
        //right-upper
        if (squareX + 1 < board.getSideSize() && squareY - 1 >= 0)
            list.add(board.getSquare(squareX + 1, squareY - 1));
        //left-downer
        if (squareX - 1 >= 0 && squareY + 1 < board.getSideSize())
            list.add(board.getSquare(squareX - 1, squareY + 1));
        //right-downer
        if (squareX + 1 < board.getSideSize() && squareY + 1 < board.getSideSize())
            list.add(board.getSquare( squareX + 1, squareY + 1));

        return list;
    }

    public ArrayList<Square> getCloseAdjacentTiles(Board board) {

        ArrayList<Square> list = new ArrayList<Square>();

        int squareX = getX();
        int squareY = getY();

        //left
        if (squareX - 1 >= 0)
            list.add(board.getSquare(squareX - 1, squareY));
        //right
        if (squareX + 1 < board.getSideSize())
            list.add(board.getSquare(squareX + 1, squareY));
        //up
        if (squareY - 1 >= 0)
            list.add(board.getSquare(squareX, squareY - 1));
        //down
        if (squareY + 1 < board.getSideSize())
            list.add(board.getSquare(squareX, squareY + 1));

        return list;
    }

    public char draw(boolean visible) {
        char square = ' ';
        switch (status) {
            case Ship:
                if (visible)
                    square = 'S';
                else
                    square = 'W';
                break;
            case Miss:
                square = 'M';
                break;
            case Water:
                square = 'W';
                break;
            case TooClose:
                square = 'T';
                break;
            case BombedShip:
                square = 'B';
                break;
        }
        return square;
    }
}
