package cz.cvut.fel.sit.battleship.ShipTypes;


public class Cruiser extends Ship {
    {
        super.setSize(3);
        super.setAliveState(true);
    }

    @Override
    public Object clone() {
        return new Cruiser();
    }
}
