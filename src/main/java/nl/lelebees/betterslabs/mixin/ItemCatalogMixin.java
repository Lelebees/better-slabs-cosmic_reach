package nl.lelebees.betterslabs.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.ui.ItemCatalog;
import finalforeach.cosmicreach.world.blocks.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemCatalog.class)
public class ItemCatalogMixin {

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/ui/ItemCatalog;addItemStack(Lfinalforeach/cosmicreach/items/ItemStack;)Z"))
    private boolean excludeMultipleVerticalSlabs(ItemCatalog instance, ItemStack itemStack, Operation<Boolean> original, @Local BlockState state) {
        if (state.stringId.contains("slab_type=vertical") && !state.stringId.contains("verticalNegZ")) {
            return false;
        }
        return original.call(instance, itemStack);
    }
}
