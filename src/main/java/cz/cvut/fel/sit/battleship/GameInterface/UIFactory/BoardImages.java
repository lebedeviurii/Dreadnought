package cz.cvut.fel.sit.battleship.GameInterface.UIFactory;

import javafx.scene.image.Image;

public class BoardImages {

    public static Image HIT_IMAGE;
    public static Image MISS_IMAGE;
    public static Image SHIP_IMAGE;
    public static Image WATER_IMAGE;

    static {
        HIT_IMAGE = new Image("HIT.png");
        MISS_IMAGE = new Image("MISS.png");
        SHIP_IMAGE = new Image("SHIP.png");
        WATER_IMAGE = new Image("WATER.png");
    }

    public Image getImageByChar(char square) {
        return switch (square) {
            case 'S' -> SHIP_IMAGE;
            case 'M' -> MISS_IMAGE;
            case 'W', 'T' -> WATER_IMAGE;
            case 'B' -> HIT_IMAGE;
            default -> WATER_IMAGE;
        };
    }
}
