package nl.lelebees.betterslabs.extras;

import finalforeach.cosmicreach.world.Direction;
import finalforeach.cosmicreach.world.entities.Entity;

import static finalforeach.cosmicreach.world.Direction.*;

public enum ViewDirection {
    NORTH("NegX", NEG_X),
    SOUTH("PosX", POS_X),
    EAST("PosZ", POS_Z),
    WEST("NegZ", NEG_Z),
    UP("PosY", POS_Y),
    DOWN("NegY", NEG_Y);

    private final String directionString;
    private final Direction direction;

    ViewDirection(String directionString, Direction direction) {
        this.directionString = directionString;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getDirectionString() {
        return directionString;
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
