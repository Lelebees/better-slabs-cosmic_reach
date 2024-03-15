package nl.lelebees.betterslabs.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.BlockSelection;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.blocks.BlockState;
import nl.lelebees.betterslabs.extras.ViewDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nl.lelebees.betterslabs.extras.LeleUtil.fetchNewState;

@Mixin(BlockSelection.class)
public class BlockPlacementMixin {

    @Inject(method = "placeBlock", at = @At("HEAD"))
    private void verticalSlabs(World world, BlockState targetBlockState, BlockPosition blockPos, double timeSinceLastInteract, CallbackInfo ci, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        BlockState blockState = blockStateLocalRef.get();
        if (!blockState.stringId.contains("type=vertical")) {
            return;
        }
        String newOrientation = "vertical" + ViewDirection.getViewDirection(InGame.getLocalPlayer().getEntity()).getOrientation();
        blockStateLocalRef.set(fetchNewState(blockState, newOrientation));
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
