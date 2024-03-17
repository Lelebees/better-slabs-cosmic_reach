package nl.lelebees.betterslabs.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.items.ItemCatalog;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.world.blocks.BlockState;
import nl.lelebees.betterslabs.extras.ViewDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemCatalog.class)
public class ItemCatalogMixin {

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/ItemCatalog;addItemStack(Lfinalforeach/cosmicreach/items/ItemStack;)Z"))
    private boolean excludeMultipleVerticalSlabs(ItemCatalog instance, ItemStack itemStack, Operation<Boolean> original, @Local BlockState state) {
        if (state.stringId.contains("type=vertical") && !state.stringId.contains(ViewDirection.WEST.getOrientation())) {
            return false;
        }
        return original.call(instance, itemStack);
    }
}
