package nl.lelebees.betterslabs.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.BlockSelection;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.blocks.BlockState;
import nl.lelebees.betterslabs.extras.ViewDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static nl.lelebees.betterslabs.extras.LeleUtil.fetchNewState;

@Mixin(BlockSelection.class)
public class BlockPlacementMixin {

    @ModifyArg(method = "raycast", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/BlockSelection;placeBlock(Lfinalforeach/cosmicreach/world/World;Lfinalforeach/cosmicreach/world/blocks/BlockState;Lfinalforeach/cosmicreach/world/BlockPosition;D)V"), index = 1)
    private BlockState adjustBlockstate(BlockState targetBlockState) {
        if (!targetBlockState.stringId.contains("type=vertical")) {
            return targetBlockState;
        }
        String newOrientation = "vertical" + ViewDirection.getViewDirection(InGame.getLocalPlayer().getEntity()).getOrientation();
        return fetchNewState(targetBlockState, newOrientation);
    }

    @WrapOperation(method = "raycast", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/BlockSelection;placeBlock(Lfinalforeach/cosmicreach/world/World;Lfinalforeach/cosmicreach/world/blocks/BlockState;Lfinalforeach/cosmicreach/world/BlockPosition;D)V"))
    private void horizontalSlabs(BlockSelection instance, World world, BlockState targetBlockState, BlockPosition targetBlockPos, double timeSinceBlockModify, Operation<Void> original, @Local(name = "breakingBlockPos") BlockPosition selectedBlockPosition) {
        // TODO: If the face is side, place the slab corresponding to the half of the face the player is looking at (top half > top slab, bottom half > bottom slab)
        if (!targetBlockState.stringId.contains("type=bottom") && !targetBlockState.stringId.contains("type=top")) {
            original.call(instance, world, targetBlockState, targetBlockPos, timeSinceBlockModify);
            return;
        }
        int deltaY = targetBlockPos.getGlobalY() - selectedBlockPosition.getGlobalY();

        String[] blockStateId = targetBlockState.stringId.split("=");
        String orientation = blockStateId[blockStateId.length - 1];
        orientation = determineOrientation(deltaY, orientation);
        original.call(instance, world, fetchNewState(targetBlockState, orientation), targetBlockPos, timeSinceBlockModify);
    }

    @Unique
    private String determineOrientation(int deltaY, String originalOrientation) {
        if (deltaY == 0) {
            // TODO: determine if it should be a bottom or top slab here
            System.out.println("side!");
            return originalOrientation;
        }
        if (deltaY < 0) {
            return "top";
        }
        return "bottom";
    }
}
