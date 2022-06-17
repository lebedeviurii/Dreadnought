package cz.cvut.fel.sit.battleship.GameConnection;

public enum ClientProtocol {
    SUBMIT,
    NAME,
    BOARD,
    HIT,
    QUIT,

    //Server's answers

    MESSAGE,
    ACCEPTED,
    REJECTED,
    FIRST,
    PREPARED,
    BOARDED1,
    PREPARED_OPPONENT,
    GAME
}
