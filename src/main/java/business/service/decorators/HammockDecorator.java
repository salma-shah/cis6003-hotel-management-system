package business.service.decorators;

import business.service.IRoom;

public class HammockDecorator  extends RoomDecorator {
    public HammockDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "A cozy hammock to experience ultimate languidity.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 1460.00;
    }
}
