package business.service.decorators;

import business.service.IRoom;

// decorator component which other decorators will use ; its the base
public abstract class RoomDecorator implements IRoom {
    protected IRoom decoratedRoom;
    public RoomDecorator(IRoom decoratedRoom) {
        this.decoratedRoom = decoratedRoom;
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription();
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost();
    }
}
