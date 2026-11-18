package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.mehvahdjukaar.smooth_animations.client.IJitterFixTimer;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DeltaTracker.Timer.class)
public abstract class DeltaTrackerTimerMixin implements IJitterFixTimer {
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

    @Override
    public void smoothAnim$freezeTick() {
        //puts the tick back, next advanceTime returns it again
        deltaTickResidual += 1;
    }

    //residual is over 1 while a tick is held. Keep at one
    @ModifyReturnValue(method = "getGameTimeDeltaPartialTick", at = @At("RETURN"))
    private float smoothAnim$stayOnLastTickWhileHeld(float partial) {
        return Math.min(1, partial);
    }
}
