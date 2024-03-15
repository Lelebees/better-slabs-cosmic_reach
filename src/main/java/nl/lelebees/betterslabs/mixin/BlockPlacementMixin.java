package nl.lelebees.betterslabs.mixin;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.collision.BoundingBox;
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

@Mixin(BlockSelection.class)
public class BlockPlacementMixin {

    @Inject(method = "placeBlock", at = @At("HEAD"))
    private void verticalSlabs(World world, BlockState targetBlockState, BlockPosition blockPos, double timeSinceLastInteract, CallbackInfo ci, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        BlockState blockState = blockStateLocalRef.get();
        if (!blockState.stringId.contains("type=vertical")) {
            return;
        }
        String newOrientation = ViewDirection.getViewDirection(InGame.getLocalPlayer().getEntity()).getOrientation();

        String[] blockStateId = blockState.stringId.split("=");
        StringBuilder stateIdBuilder = new StringBuilder();
        for (String string : blockStateId) {
            if (string.equals(blockStateId[blockStateId.length - 1])) {
                stateIdBuilder.append(newOrientation);
                continue;
            }
            stateIdBuilder.append(string).append("=");
        }

        String saveKey = blockState.getBlockId() + "[" + stateIdBuilder + "]";
        blockState = BlockState.getInstance(saveKey);
        blockStateLocalRef.set(blockState);
    }

    @Inject(method = "raycast", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/BlockSelection;placeBlock(Lfinalforeach/cosmicreach/world/World;Lfinalforeach/cosmicreach/world/blocks/BlockState;Lfinalforeach/cosmicreach/world/BlockPosition;D)V"))
    private void horizontalSlabs(World world, Camera worldCamera, CallbackInfo ci, @Local(name = "breakingBlockPos") BlockPosition highlightedBlockPosition) {
        /* TODO: We'll need to look at the highlightedBlockPosition, and its Block.
        We want to determine what face the player is (was) looking at, and if the face was a side face, what half of the face.
        If the face is bottom or top, place the opposite slab (bottom face > top slab, top face > bottom slab)
        If the face is side, place the slab corresponding to the half of the face the player is looking at
        (top half > top slab, bottom half > bottom slab) */
        BoundingBox boundingBox = highlightedBlockPosition.getBlockState().getModel().boundingBox;
    }
}
