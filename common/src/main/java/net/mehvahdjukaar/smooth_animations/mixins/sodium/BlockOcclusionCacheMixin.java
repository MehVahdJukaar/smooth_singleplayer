package net.mehvahdjukaar.smooth_animations.mixins.sodium;

import com.llamalad7.mixinextras.sugar.Local;
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

//like in a goodplace
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache", remap = false, priority = 600 ) //for more cullint
public abstract class BlockOcclusionCacheMixin {

    @Inject(
            method = "shouldDrawSide",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    remap = true,
                    target = "Lnet/minecraft/core/BlockPos$MutableBlockPos;set(III)Lnet/minecraft/core/BlockPos$MutableBlockPos;"),
            remap = false,
            cancellable = true
    )
    private void smoothANim$overrideCulling(BlockState selfState, BlockGetter view, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir,
                                            @Local BlockPos.MutableBlockPos cpos) {
        if (BlockMeshHider.isHidden(cpos)){
            cir.setReturnValue(true);
        }
    }
}
