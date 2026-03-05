package business.service.decorators;

import business.service.IRoom;

public class BalconyDecorator  extends RoomDecorator {
    public BalconyDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "A balcony with a gorgeous view of the ocean..";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 800.00;
    }
}
