package net.mehvahdjukaar.smooth_animations.mixins.fabric.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.mehvahdjukaar.smooth_animations.client.BlockMeshHider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = BlockOcclusionCache.class, priority = 600)
public abstract class BlockOcclusionCacheMixin {

    @Inject(
            method = "shouldDrawSide",
            at = @At(value = "INVOKE",
                    shift = At.Shift.BEFORE, remap = true,
                    target = "Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
            remap = false,
            cancellable = true
    )
    private void smoothAnim$overrideCulling(BlockState selfState, BlockGetter view, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir,
                                            @Local BlockPos.MutableBlockPos cpos) {
        if (BlockMeshHider.isHidden(cpos)) {
            cir.setReturnValue(true);
        }
    }
}
