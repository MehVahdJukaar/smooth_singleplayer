package net.mehvahdjukaar.smooth_animations.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public record LandingData(BlockPos pos, @Nullable SectionRenderDispatcher.RenderSection section,
                          @Nullable SectionMesh oldMesh) {

    public static LandingData capture(BlockPos pos) {
        var section = Minecraft.getInstance().levelRenderer.viewArea.getRenderSectionAt(pos);
        var c = (section == null) ? null : section.sectionMesh.get();

        return new LandingData(pos, section, c);
    }

    public boolean isNoLongerWaitingMesh() {
        //eotjer mp section exits or compiled is new
        if (section == null || section.sectionMesh.get() != oldMesh) return true;
        //sodium leaves vanilla sections like this forever
        return oldMesh == CompiledSectionMesh.UNCOMPILED;
    }
}
