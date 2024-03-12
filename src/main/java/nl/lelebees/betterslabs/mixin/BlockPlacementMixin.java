package nl.lelebees.betterslabs.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.BlockSelection;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.blocks.BlockState;
import finalforeach.cosmicreach.world.entities.Entity;
import nl.lelebees.betterslabs.extras.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static nl.lelebees.betterslabs.extras.Direction.*;

@Mixin(BlockSelection.class)
public class BlockPlacementMixin {

    @Inject(method = "placeBlock", at = @At("HEAD"))
    private void injected(World world, BlockState targetBlockState, BlockPosition blockPos, CallbackInfo ci, @Local LocalRef<BlockState> blockStateLocalRef) {
        BlockState blockState = blockStateLocalRef.get();
        if (!blockState.stringId.contains("vertical")) {
            return;
        }

        Entity playerEntity = InGame.getLocalPlayer().getEntity();
        double yaw = -Math.atan2(playerEntity.viewDirection.x, playerEntity.viewDirection.z) / Math.PI * 180.0;
        String newOrientation = getOrientation(yaw);

        String[] blockStateId = blockState.stringId.split("=");
        StringBuilder stateIdBuilder = new StringBuilder();
        Arrays.stream(blockStateId).forEach(string -> {
            if (string.equals(blockStateId[blockStateId.length - 1])) {
                stateIdBuilder.append(newOrientation);
                return;
            }
            stateIdBuilder.append(string).append("=");
        });

        blockState.stringId = stateIdBuilder.toString();
        blockState = BlockState.getInstance(blockState.getSaveKey());
        blockStateLocalRef.set(blockState);
        // TODO: prevent multiple vertical slab orientations from showing up. just use the same one :)
    }

    @Unique
    private Direction calculateDirection(double yaw) {
        if (yaw < -135.0 || yaw > 135.0) {
            return WEST;
        }
        if (yaw < -45.0) {
            return SOUTH;
        }
        return yaw < 45.0 ? EAST : NORTH;
    }

    @Unique
    private String getOrientation(double yaw) {
        return calculateDirection(yaw).getOrientation();
    }

}
