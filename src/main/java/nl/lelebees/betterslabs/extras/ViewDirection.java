package nl.lelebees.betterslabs.extras;

import finalforeach.cosmicreach.world.Direction;
import finalforeach.cosmicreach.world.entities.Entity;

import static finalforeach.cosmicreach.world.Direction.*;

public enum ViewDirection {
    NORTH("verticalNegX", NEG_X),
    SOUTH("verticalPosX", POS_X),
    EAST("verticalPosZ", POS_Z),
    WEST("verticalNegZ", NEG_Z);

    private final String orientation;
    private final Direction direction;

    ViewDirection(String orientation, Direction direction) {
        this.orientation = orientation;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getOrientation() {
        return orientation;
    }

    public static ViewDirection getViewDirection(Entity entity) {
        double yaw = -Math.atan2(entity.viewDirection.x, entity.viewDirection.z) / Math.PI * 180.0;
        if (yaw < -135.0 || yaw > 135.0) {
            return WEST;
        }
        if (yaw < -45.0) {
            return SOUTH;
        }
        return yaw < 45.0 ? EAST : NORTH;
    }
}
