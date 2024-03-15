package nl.lelebees.betterslabs.mixin;


import com.badlogic.gdx.graphics.Camera;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.BlockSelection;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.blocks.BlockState;
import nl.lelebees.betterslabs.extras.ViewDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nl.lelebees.betterslabs.extras.LeleUtil.changeState;

@Mixin(BlockSelection.class)
public class BlockPlacementMixin {

    @Inject(method = "placeBlock", at = @At("HEAD"))
    private void verticalSlabs(World world, BlockState targetBlockState, BlockPosition blockPos, double timeSinceLastInteract, CallbackInfo ci, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        BlockState blockState = blockStateLocalRef.get();
        if (!blockState.stringId.contains("type=vertical")) {
            return;
        }
        String newOrientation = "vertical" + ViewDirection.getViewDirection(InGame.getLocalPlayer().getEntity()).getOrientation();

        blockStateLocalRef.set(changeState(blockState, newOrientation));
    }


    @Inject(method = "raycast", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/BlockSelection;placeBlock(Lfinalforeach/cosmicreach/world/World;Lfinalforeach/cosmicreach/world/blocks/BlockState;Lfinalforeach/cosmicreach/world/BlockPosition;D)V"))
    private void horizontalSlabs(World world, Camera worldCamera, CallbackInfo ci, @Local(name = "breakingBlockPos") BlockPosition selectedBlockPosition, @Local(name = "placingBlockPos") BlockPosition placingBlockPosition, @Local(name = "targetBlockState") LocalRef<BlockState> targetBlockStateRef) {
        /* TODO: We'll need to look at the selectedBlockPosition, and its Block.
        We want to determine what face the player is (was) looking at, and if the face was a side face, what half of the face.
        If the face is bottom or top, place the opposite slab (bottom face > top slab, top face > bottom slab)
        If the face is side, place the slab corresponding to the half of the face the player is looking at
        (top half > top slab, bottom half > bottom slab) */
        BlockState targetBlockState = targetBlockStateRef.get();
        if (!targetBlockState.stringId.contains("type=bottom") && !targetBlockState.stringId.contains("type=top")) {
            return;
        }
        int deltaX = placingBlockPosition.getGlobalX() - selectedBlockPosition.getGlobalX();
        int deltaZ = placingBlockPosition.getGlobalZ() - selectedBlockPosition.getGlobalZ();
        int deltaY = placingBlockPosition.getGlobalY() - selectedBlockPosition.getGlobalY();

        String[] blockStateId = targetBlockState.stringId.split("=");
        String orientation = blockStateId[blockStateId.length - 1];
        System.out.println("old orientation: " + orientation);
        if (deltaX != 0 || deltaZ != 0) {
            System.out.println("Side");
        }
        if (deltaY != 0) {
            System.out.println("Top/bottom");
            if (deltaY < 0) {
                System.out.println("bottom!");
                orientation = "top";
            }
            if (deltaY > 0) {
                System.out.println("top!");
                orientation = "bottom";
            }
        }
        System.out.println("new orientation:" + orientation);
        targetBlockStateRef.set(changeState(targetBlockState, orientation));
    }
}
