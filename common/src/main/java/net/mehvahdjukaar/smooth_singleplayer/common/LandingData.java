package net.mehvahdjukaar.smooth_singleplayer.common;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public record LandingData(BlockPos pos, @Nullable SectionRenderDispatcher.RenderSection section,
                          @Nullable SectionRenderDispatcher.CompiledSection oldMesh) {

    public boolean isNoLongerWaitingMesh() {
        //eotjer mp section exits or compiled is new
        return section == null || section.compiled.get() != oldMesh;
    }
}
