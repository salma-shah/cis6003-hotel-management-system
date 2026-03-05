package business.service.decorators;

import business.service.IRoom;

public class MinibarDecorator extends RoomDecorator {
    public MinibarDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "A minibar stacked with the finest chocolates and refreshing sodas.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 12900.00;
    }
}
