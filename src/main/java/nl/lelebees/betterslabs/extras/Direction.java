package nl.lelebees.betterslabs.extras;

public enum Direction {
    NORTH("verticalNegX"),
    SOUTH("verticalPosX"),
    EAST("verticalPosZ"),
    WEST("verticalNegZ");

    private final String orientation;

    Direction(String orientation) {
        this.orientation = orientation;
    }

    public String getOrientation() {
        return orientation;
    }
}
