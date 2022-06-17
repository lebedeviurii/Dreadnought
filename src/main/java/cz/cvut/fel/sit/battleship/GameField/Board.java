package cz.cvut.fel.sit.battleship.GameField;

public class Board {
    public int sideSize;
    private Square[][] board;
    private boolean visible;
    public boolean currentOrientationHorizontal = true;
    public void setSideSize(int sideSize) {
        this.sideSize = sideSize;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getSideSize() {
        return sideSize;
    }

    public Square[][] getBoard() {
        return board;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    public boolean isVisible() {
        return visible;
    }

    //Constructor
    public void boardConstruction(boolean visible, int size) {
        this.sideSize = size;
        this.visible = visible;
        this.board = new Square[sideSize][sideSize];
        squareConstruction();
    }

    private void squareConstruction() {
        for (int i = 0; i < sideSize; i++) {
            for (int y = 0; y < sideSize; y++) {
                board[i][y] = new Square(i, y, SquareStatus.Water);
            }
        }
    }

    public void boardSetting(String[][] statuses) {}

    public boolean getCurrentOrientationHorizontal() {
        return currentOrientationHorizontal;
    }

    public void setCurrentOrientation(boolean currentOrientationHorizontal) {
        this.currentOrientationHorizontal = currentOrientationHorizontal;
    }

    //Getter position
    public Square getPosition(int x, int y) {
        if (x < sideSize && x >= 0 && y < sideSize && y >= 0 && board != null && board[x][y] != null){
            return board[x][y];
        }
        return null;
    }

    public SquareStatus getPositionStatus(int x, int y) {
        if (x < sideSize && x >= 0 && y < sideSize && y >= 0 && board != null && board[x][y] != null){
            return board[x][y].getStatus();
        }
        return SquareStatus.None;
    }


    //Check for any alive ships
    public boolean allShipsSunk() {
        for (int i = 0; i < sideSize; i++)
            for (int j = 0; j < sideSize; j++)
                if (board[i][j].getStatus() == SquareStatus.Ship)
                    return false;
        return true;
    }


    public String boardToString() {
        String bS = " ";
        for (int i = 0; i < sideSize; i++) {
            for (int j = 0; j < sideSize; j++) {
                bS += board[i][j].getStatus() + "(" + i + ";";
                if ((i != sideSize - 1) && (j != sideSize - 1)) {
                    bS += ",";
                }
            }
        }
        return bS;
    }
}

