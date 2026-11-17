package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.mehvahdjukaar.smooth_animations.client.BlockMeshHider;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderSectionRegion.class)
public abstract class RenderSectionRegionMixin {

    @ModifyReturnValue(method = "getBlockState", at = @At("RETURN"))
    private BlockState ss$hideLandingFallingBlocks(BlockState original, BlockPos pos) {
        return BlockMeshHider.isHidden(pos) ? Blocks.AIR.defaultBlockState() : original;
    }
}
