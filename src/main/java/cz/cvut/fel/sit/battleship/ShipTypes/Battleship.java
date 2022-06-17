package cz.cvut.fel.sit.battleship.ShipTypes;


public class Battleship extends Ship {
    {
        super.setSize(4);
        super.setAliveState(true);
    }

    @Override
    public Object clone() {
        return new Battleship();
    }
}
