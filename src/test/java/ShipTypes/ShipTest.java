package ShipTypes;

import cz.cvut.fel.sit.battleship.GameField.Board;
import cz.cvut.fel.sit.battleship.GameField.Square;
import cz.cvut.fel.sit.battleship.GameField.SquareStatus;
import cz.cvut.fel.sit.battleship.ShipTypes.Orientation;
import cz.cvut.fel.sit.battleship.ShipTypes.OverlapException;
import cz.cvut.fel.sit.battleship.ShipTypes.Ship;
import cz.cvut.fel.sit.battleship.ShipTypes.UnableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ShipTest {
    Ship ship;
    Square square;
    Board board;

    @BeforeEach
    public void setUp() {
        ship = mock(Ship.class);
        square = mock(Square.class);
        board = mock(Board.class);
    }

    // UnableException tests
    @Test
    public void isAvailable_horizontalShipYIsMinus1_UnableException() throws UnableException {
        when(square.getY()).thenReturn(-1);
        when(ship.getShipOrientation()).thenReturn(Orientation.horizontal);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (ship.getShipOrientation() == Orientation.horizontal) {
                    if (square.getY() < 0)
                        throw new UnableException();
                }
                return null;
            }
        }).when(ship).isAvailable();

        Assertions.assertThrows(UnableException.class, () -> {
            ship.isAvailable();
        });
    }

    @Test
    public void isAvailable_horizontalShipXIsMinus1_UnableException() throws UnableException {
        when(square.getX()).thenReturn(-1);
        when(ship.getShipOrientation()).thenReturn(Orientation.horizontal);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (ship.getShipOrientation() == Orientation.horizontal) {
                    if (square.getX() < 0)
                        throw new UnableException();
                }
                return null;
            }
        }).when(ship).isAvailable();

        Assertions.assertThrows(UnableException.class, () -> {
            ship.isAvailable();
        });
    }

    @Test
    public void isAvailable_verticalShipXIsMinus1_UnableException() throws UnableException {
        when(square.getX()).thenReturn(-1);
        when(ship.getShipOrientation()).thenReturn(Orientation.vertical);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (ship.getShipOrientation() == Orientation.vertical) {
                    if (square.getX() < 0)
                        throw new UnableException();
                }
                return null;
            }
        }).when(ship).isAvailable();

        Assertions.assertThrows(UnableException.class, () -> {
            ship.isAvailable();
        });
    }

    @Test
    public void isAvailable_verticalShipYIsMinus1_UnableException() throws UnableException {
        when(square.getY()).thenReturn(-1);
        when(ship.getShipOrientation()).thenReturn(Orientation.vertical);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (ship.getShipOrientation() == Orientation.vertical) {
                    if (square.getY() < 0)
                        throw new UnableException();
                }
                return null;
            }
        }).when(ship).isAvailable();

        Assertions.assertThrows(UnableException.class, () -> {
            ship.isAvailable();
        });
    }

    // OverlapException tests
    @Test
    public void isOverlap_horizontalShipVsSquareStatusShip_OverlapException() throws OverlapException {
        when(square.getStatus()).thenReturn(SquareStatus.Ship);
        when(ship.getShipOrientation()).thenReturn(Orientation.horizontal);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if(ship.getShipOrientation() == Orientation.horizontal){
                    if(square.getStatus() == SquareStatus.Ship)
                        throw new OverlapException();
                }
                return null;
            }
        }).when(ship).isOverlap(board);

        Assertions.assertThrows(OverlapException.class, () -> {
            ship.isOverlap(board);
        });
    }

    @Test
    public void isOverlap_verticalShipVsSquareStatusTooClose_OverlapException() throws OverlapException {
        when(square.getStatus()).thenReturn(SquareStatus.TooClose);
        when(ship.getShipOrientation()).thenReturn(Orientation.vertical);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if(ship.getShipOrientation() == Orientation.vertical){
                    if(square.getStatus() == SquareStatus.TooClose)
                        throw new OverlapException();
                }
                return null;
            }
        }).when(ship).isOverlap(board);

        Assertions.assertThrows(OverlapException.class, () -> {
            ship.isOverlap(board);
        });
    }
}
