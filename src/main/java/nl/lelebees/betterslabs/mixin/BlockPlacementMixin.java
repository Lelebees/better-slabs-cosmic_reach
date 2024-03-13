package nl.lelebees.betterslabs.mixin;


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
    private void injected(World world, BlockState targetBlockState, BlockPosition blockPos, double timeSinceLastInteract, CallbackInfo ci, @Local LocalRef<BlockState> blockStateLocalRef) {
        BlockState blockState = blockStateLocalRef.get();
        if (!blockState.stringId.contains("vertical")) {
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
}
