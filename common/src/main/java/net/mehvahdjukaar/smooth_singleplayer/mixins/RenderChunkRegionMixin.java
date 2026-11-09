package net.mehvahdjukaar.smooth_singleplayer.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.mehvahdjukaar.smooth_singleplayer.client.BlockMeshHider;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderChunkRegion.class)
public abstract class RenderChunkRegionMixin {

    @ModifyReturnValue(method = "getBlockState", at = @At("RETURN"))
    private BlockState ss$hideLandingFallingBlocks(BlockState original, BlockPos pos) {
        //TODO: steal from a good place to make it work on sodium
        return BlockMeshHider.isHidden(pos) ? Blocks.AIR.defaultBlockState() : original;
    }
}
