module Dreadnought {

    requires javafx.controls;
    requires java.logging;
    requires java.desktop;

    exports cz.cvut.fel.sit.battleship;
    opens cz.cvut.fel.sit.battleship.GameInterface;
    opens cz.cvut.fel.sit.battleship.GameInterface.model;
    exports cz.cvut.fel.sit.battleship.GameConfiguration;
}