package cz.cvut.fel.sit.battleship.ShipTypes;


public class Convoy extends Ship {
    {
        super.setSize(1);
        super.setAliveState(true);
        super.setOrientation(Orientation.vertical);
    }

    @Override
    public Object clone() {
        return new Convoy();
    }
}
