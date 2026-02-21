package business.service.decorators;

import business.service.IRoom;
import entity.Room;

public class SpaDecorator extends RoomDecorator {
    public SpaDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "A relaxing spa experience.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 8500.00;
    }
}
