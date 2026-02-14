package business.service.decorators;

import business.service.IRoom;

public class PoolDecorator extends RoomDecorator {
    public PoolDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "\n Furthermore, the room features a private, heated pool designed for relaxation and immersion.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 15000.00;
    }
}
