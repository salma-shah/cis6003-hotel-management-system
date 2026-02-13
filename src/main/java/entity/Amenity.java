package entity;

public class Amenity implements SuperEntity {
    private final int id;
    private final String name;
    private final double cost;

    public Amenity(int id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}
