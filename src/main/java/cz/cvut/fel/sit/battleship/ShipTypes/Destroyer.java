package cz.cvut.fel.sit.battleship.ShipTypes;


public class Destroyer extends Ship {{

    super.setSize(2);
    super.setAliveState(true);
    
}

    @Override
    public Object clone() {
        return new Destroyer();
    }
}
