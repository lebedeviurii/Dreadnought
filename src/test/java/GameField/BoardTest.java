package GameField;

import cz.cvut.fel.sit.battleship.GameField.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardTest {
    Board board;

    @Test
    public void allShipSunk_newStandardBoardWithoutShips_true() {
        board = new Board();
        board.boardConstruction(true, 8);

        Assertions.assertTrue(board.allShipsSunk());
    }
}
