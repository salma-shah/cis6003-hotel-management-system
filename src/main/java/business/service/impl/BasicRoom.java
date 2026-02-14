package business.service.impl;

import business.service.IRoom;

// this is the concrete component of implemeintg decorator apttern
public class BasicRoom implements IRoom {
    @Override
    public double getCost() {
        return 20000.0;
    }

    @Override
    public String getDescription() {
        return "\n" +
                "A cozy, air-conditioned room with a comfortable bed.\n" +
                "\n" +
                "The room includes a work desk, additional sofa, coffee and tea making set, and a TV with international channels. The bathroom is equipped with a shower and a hairdryer.\n" +
                "\n" +
                "It is possible to completely darken the room.\n";
    }
}
