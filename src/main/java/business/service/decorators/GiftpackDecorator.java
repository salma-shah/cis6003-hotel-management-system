package business.service.decorators;

import business.service.IRoom;

public class GiftpackDecorator  extends RoomDecorator {
    public GiftpackDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "A daily giftpack personalized for your stay at Galle, delivered to your doorstep.";
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 2500.00;
    }
}
