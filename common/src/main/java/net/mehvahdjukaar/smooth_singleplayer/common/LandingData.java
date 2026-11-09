package net.mehvahdjukaar.smooth_singleplayer.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public record LandingData(BlockPos pos, @Nullable SectionRenderDispatcher.RenderSection section,
                          @Nullable SectionRenderDispatcher.CompiledSection oldMesh) {

    public static LandingData capture(BlockPos pos) {
        var section = Minecraft.getInstance().levelRenderer.viewArea.getRenderSectionAt(pos);
        var c = section == null ? null : section.compiled.get();

        return new LandingData(pos, section, c);
    }

    public boolean isNoLongerWaitingMesh() {
        //eotjer mp section exits or compiled is new
        return section == null || section.compiled.get() != oldMesh;
    }
}
