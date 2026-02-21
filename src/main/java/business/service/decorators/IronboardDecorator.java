package business.service.decorators;

import business.service.IRoom;

public class IronboardDecorator extends RoomDecorator {
    public IronboardDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + ", foldable Iron Board.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 1900.00;
    }
}
