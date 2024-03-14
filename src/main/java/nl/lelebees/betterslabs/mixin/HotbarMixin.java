package nl.lelebees.betterslabs.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import finalforeach.cosmicreach.ui.Hotbar;
import finalforeach.cosmicreach.world.blocks.BlockState;
import nl.lelebees.betterslabs.extras.ViewDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hotbar.class)
public class HotbarMixin {

    @Inject(method = "pickBlock", at = @At("HEAD"))
    private void blockSlabPicks(BlockState blockState, CallbackInfo ci, @Local LocalRef<BlockState> blockStateLocalRef) {
        BlockState targetBlock = blockStateLocalRef.get();
        if (!targetBlock.stringId.contains("type=vertical")) {
            return;
        }
        String orientation = ViewDirection.WEST.getOrientation();
        // TODO: YAY! reusable code!
        String[] blockStateId = targetBlock.stringId.split("=");
        StringBuilder stateIdBuilder = new StringBuilder();
        for (String string : blockStateId) {
            if (string.equals(blockStateId[blockStateId.length - 1])) {
                stateIdBuilder.append(orientation);
                continue;
            }
            stateIdBuilder.append(string).append("=");
        }
        String saveKey = targetBlock.getBlockId() + "[" + stateIdBuilder + "]";
        targetBlock = BlockState.getInstance(saveKey);
        blockStateLocalRef.set(targetBlock);
    }
}
