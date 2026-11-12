package net.mehvahdjukaar.smooth_animations.mixins;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.mehvahdjukaar.smooth_animations.client.FrameTimeCatchUp;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DeltaTracker.Timer.class)
public abstract class DeltaTrackerTimerMixin implements FrameTimeCatchUp {
    @Shadow
    private float deltaTickResidual;
    @Shadow
    private long lastMs;
    @Shadow
    @Final
    private float msPerTick;
    @Shadow
    @Final
    private FloatUnaryOperator targetMsptProvider;
    @Shadow
    private boolean paused;
    @Shadow
    private boolean frozen;

    @Override
    public void smoothAnim$catchUpPartialTick(long time) {
        if (paused || frozen) return;
        //same as advanceTime but only adds the residual missing part
        float elapsed = (time - lastMs) / targetMsptProvider.apply(msPerTick);
        deltaTickResidual = Math.min(1, deltaTickResidual + elapsed);
        lastMs = time;
    }
}
