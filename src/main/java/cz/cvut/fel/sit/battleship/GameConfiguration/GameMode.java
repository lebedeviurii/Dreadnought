package cz.cvut.fel.sit.battleship.GameConfiguration;

import java.io.IOException;

import cz.cvut.fel.sit.battleship.ShipTypes.*;


public class GameMode {
    
    public int size;

    public Ship[] ships;

    // SETTERS and GETTERS
    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Ship[] getShips() {
        return this.ships;
    }

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

    public boolean getPropValues(String mode) throws IOException
    {
        try {
            Parser p = new Parser();
            p.configParseFile(mode, this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

