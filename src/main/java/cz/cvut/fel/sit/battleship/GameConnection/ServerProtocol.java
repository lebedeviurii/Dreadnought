package cz.cvut.fel.sit.battleship.GameConnection;

public enum ServerProtocol {
    SUBMIT,
    MESSAGE,
    FIRST,
    ACCEPTED,
    REJECTED,
    PREPARED,
    BOARDED1,
    PREPARED_OPPONENT,
    GAME
}
