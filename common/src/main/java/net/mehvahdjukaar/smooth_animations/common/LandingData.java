package net.mehvahdjukaar.smooth_animations.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public record LandingData(BlockPos pos, @Nullable ChunkRenderDispatcher.RenderChunk section,
                          @Nullable ChunkRenderDispatcher.CompiledChunk oldMesh) {

    public static LandingData capture(BlockPos pos) {
        var section = Minecraft.getInstance().levelRenderer.viewArea.getRenderChunkAt(pos);
        var c = section == null ? null : section.compiled.get();

        return new LandingData(pos, section, c);
    }

    public boolean isNoLongerWaitingMesh() {
        //eotjer mp section exits or compiled is new
        if (section == null || section.compiled.get() != oldMesh) return true;
        //sodium leaves vanilla sections like this forever
        return oldMesh == ChunkRenderDispatcher.CompiledChunk.UNCOMPILED;
    }
}
