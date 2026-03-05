package business.service.decorators;

import business.service.IRoom;

public class SpaDecorator extends RoomDecorator {
    public SpaDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() +
                "A relaxing spa experience brought to your room.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 8500.00;
    }
}
