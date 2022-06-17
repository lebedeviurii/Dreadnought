package cz.cvut.fel.sit.battleship.GameInterface.util;

import cz.cvut.fel.sit.battleship.GameInterface.UIFactory.BoardImages;

public class BoardImagesSingleton {
    private static BoardImages single_instance = null;

    // Declaring a variable of type String
    public String s;

    private BoardImagesSingleton()
    {
        s = "Hello I am a string part of Singleton class";
    }

    public static BoardImages getInstance()
    {
        if (single_instance == null)
            single_instance = new BoardImages();

        return single_instance;
    }
}
