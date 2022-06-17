package GameUsers;

import cz.cvut.fel.sit.battleship.GameField.Board;
import cz.cvut.fel.sit.battleship.GameField.Square;
import cz.cvut.fel.sit.battleship.GameField.SquareStatus;
import cz.cvut.fel.sit.battleship.GameUsers.Player;
import cz.cvut.fel.sit.battleship.GameUsers.RepeatedStrikeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class PlayerTest {

    Player player;
    Board board;
    Square square;

    @BeforeEach
    public void setUp() {
        player = mock(Player.class);
        board = mock(Board.class);
        square = mock(Square.class);
    }

    @Test
    public void isFiredSquare_newBoardXIs1YIs1_RepeatedStrikeException() throws RepeatedStrikeException {
        when(board.getPosition(1, 1)).thenReturn(square);
        when(board.getPosition(1, 1).getStatus()).thenReturn(SquareStatus.Miss);
        doAnswer(invocationOnMock -> {
            if (board.getPosition(1,1).getStatus() == SquareStatus.Miss) {
                throw new RepeatedStrikeException();
            };
            return null;
        }).when(player).isFiredSquare(board, 1, 1);

        Assertions.assertThrows(RepeatedStrikeException.class, () -> {
            player.isFiredSquare(board, 1, 1);
        });
    }

    @Test
    public void fire_ship_bombedShip() {
        when(square.getStatus()).thenReturn(SquareStatus.Ship);
        int[] position = new int[2];

        doAnswer(new Answer<SquareStatus>() {
            @Override
            public SquareStatus answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (square.getStatus() == SquareStatus.Ship) {
                    return SquareStatus.BombedShip;
                }
                return null;
            }
        }).when(player).fire(board, player, position);

        Assertions.assertEquals(player.fire(board, player, position), SquareStatus.BombedShip);
    }

    @Test
    public void fire_water_miss() {
        when(square.getStatus()).thenReturn(SquareStatus.Water);
        int[] position = new int[2];

        doAnswer(new Answer<SquareStatus>() {
            @Override
            public SquareStatus answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (square.getStatus() == SquareStatus.Water) {
                    return SquareStatus.Miss;
                }
                return null;
            }
        }).when(player).fire(board, player, position);

        Assertions.assertEquals(player.fire(board, player, position), SquareStatus.Miss);
    }
}
