package net.mehvahdjukaar.smooth_animations.mixins.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.smooth_animations.client.BlockMeshHider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//like in a goodplace
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext", remap = false, priority = 600)
public abstract class BlockOcclusionCacheMixin {

    @Inject(
            method = "shouldDrawSide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockAndTintGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            remap = false
    )
    private void smoothANim$overrideCulling(Direction facing, CallbackInfoReturnable<Boolean> cir,
                                     @Local BlockPos.MutableBlockPos neighborPos) {
        if (BlockMeshHider.isHidden(neighborPos)) {
            cir.setReturnValue(true);
        }
    }
}
