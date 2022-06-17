package cz.cvut.fel.sit.battleship.ShipTypes;


public class AircraftCarrier extends Ship {
    {
        super.setSize(5);
        super.setAliveState(true);

    }

    @Override
    public Object clone() {
        return new AircraftCarrier();
    }
}
