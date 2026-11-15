package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.client.FrameTimeCatchUp;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Timer.class)
public abstract class TimerMixin implements FrameTimeCatchUp {
    @Shadow
    public float partialTick;
    @Shadow
    private long lastMs;
    @Shadow
    @Final
    private float msPerTick;

    @Override
    public void smoothAnim$catchUpPartialTick(long time) {
        //same as advanceTime but only adds the residual missing part
        float elapsed = (time - lastMs) / msPerTick;
        partialTick = Math.min(1, partialTick + elapsed);
        lastMs = time;
    }
}
