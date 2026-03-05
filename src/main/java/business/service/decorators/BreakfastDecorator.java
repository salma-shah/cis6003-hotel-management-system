package business.service.decorators;

import business.service.IRoom;
import entity.Room;

public class BreakfastDecorator extends RoomDecorator{
    public BreakfastDecorator(IRoom decoratedRoom) {
        super(decoratedRoom);
    }

    @Override
    public double getCost() {
        return decoratedRoom.getCost() + 5500.00;
    }

    @Override
    public String getDescription() {
        return decoratedRoom.getDescription() + "Additionally, our Bed & Breakfast package is included. Daily breakfast is included, from " +
                "7AM-11AM, with options ranging from a quick continental grab to full meals featuring locally-sourced ingredients.";
    }
}
