package Entities;


public enum Location {
    NORTH, EAST, SOUTH, WEST;

    public static Location getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
