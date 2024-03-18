package nl.lelebees.betterslabs.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.BlockSelection;
import finalforeach.cosmicreach.world.blocks.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static nl.lelebees.betterslabs.extras.LeleUtil.fetchNewState;
import static nl.lelebees.betterslabs.extras.ViewDirection.getViewDirection;

@Mixin(BlockSelection.class)
public class BlockPlacementMixin {

    @ModifyVariable(method = "placeBlock", at = @At("HEAD"), argsOnly = true)
    private BlockState adjustBlockstateVertical(BlockState targetBlockState) {
        if (!targetBlockState.stringId.contains("type=vertical")) {
            return targetBlockState;
        }
        String newOrientation = "vertical" + getViewDirection(InGame.getLocalPlayer().getEntity()).getDirectionString();
        return fetchNewState(targetBlockState, newOrientation);
    }

    @ModifyArg(method = "raycast", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/BlockSelection;placeBlock(Lfinalforeach/cosmicreach/world/World;Lfinalforeach/cosmicreach/world/blocks/BlockState;Lfinalforeach/cosmicreach/world/BlockPosition;D)V"), index = 1)
    private BlockState adjustBlockstateHorizontal(BlockState targetBlockState, @Local(name = "breakingBlockPos") BlockPosition selectedBlockPosition, @Local(name = "placingBlockPos") BlockPosition targetBlockPosition) {
        // TODO: If the face is side, place the slab corresponding to the half of the face the player is looking at (top half > top slab, bottom half > bottom slab)
        if (!targetBlockState.stringId.contains("type=bottom") && !targetBlockState.stringId.contains("type=top")) {
            return targetBlockState;
        }
        int deltaY = targetBlockPosition.getGlobalY() - selectedBlockPosition.getGlobalY();

        String[] blockStateId = targetBlockState.stringId.split("=");
        String orientation = blockStateId[blockStateId.length - 1];
        return fetchNewState(targetBlockState, determineOrientation(deltaY, orientation));
    }

    //TODO: move to util?
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
