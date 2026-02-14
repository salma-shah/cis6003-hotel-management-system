package business.service.decorators;

import business.service.IRoom;

public class WifiDecorator extends RoomDecorator{

    public WifiDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "A Wifi router is included in the package, proving 5G wifi.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 2500.00;
    }
}
