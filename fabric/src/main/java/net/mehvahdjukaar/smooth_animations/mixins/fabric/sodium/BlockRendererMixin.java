package net.mehvahdjukaar.smooth_animations.mixins.fabric.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.mehvahdjukaar.smooth_animations.client.BlockMeshHider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin {

    @Inject(
            method = "renderModel",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void smoothAnim$hideBlock(BlockRenderContext ctx, ChunkBuildBuffers buffers, CallbackInfo ci) {
        if (BlockMeshHider.isHidden(ctx.pos())) {
            ci.cancel();
        }
    }
}
