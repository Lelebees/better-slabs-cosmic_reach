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

import static nl.lelebees.betterslabs.extras.LeleUtil.changeState;

@Mixin(Hotbar.class)
public class HotbarMixin {

    @Inject(method = "pickBlock", at = @At("HEAD"))
    private void blockSlabPicks(BlockState blockState, CallbackInfo ci, @Local(argsOnly = true) LocalRef<BlockState> blockStateLocalRef) {
        BlockState targetBlock = blockStateLocalRef.get();
        if (!targetBlock.stringId.contains("type=vertical")) {
            return;
        }
        String orientation = "vertical" + ViewDirection.WEST.getOrientation();
        blockStateLocalRef.set(changeState(targetBlock, orientation));
    }
}
