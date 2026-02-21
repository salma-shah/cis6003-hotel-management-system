package business.service.decorators;

import business.service.IRoom;

public class BathtubDecorator  extends RoomDecorator {
    public BathtubDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "A bathtub to have a relaxing soak in.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 1820.00;
    }
}
