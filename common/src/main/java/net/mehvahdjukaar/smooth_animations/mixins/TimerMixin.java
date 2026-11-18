package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.client.IJitterFixTimer;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Timer.class)
public abstract class TimerMixin implements IJitterFixTimer {
    @Shadow
    public float partialTick;
    @Shadow
    private long lastMs;
    @Shadow
    @Final
    private float msPerTick;

    @Unique
    private int smoothAnim$heldTicks = 0;
    @Unique
    private float smoothAnim$heldResidual;

    @Override
    public void smoothAnim$catchUpPartialTick(long time) {
        //same as advanceTime but only adds the residual missing part
        float elapsed = (time - lastMs) / msPerTick;
        partialTick = Math.min(1, partialTick + elapsed);
        lastMs = time;
    }

    @Override
    public void smoothAnim$freezeTick() {
        if (smoothAnim$heldTicks == 0) smoothAnim$heldResidual = partialTick;
        smoothAnim$heldTicks++;
        //partialTick is a public field read everywhere so we cant just let it go over 1
        partialTick = 1;
    }

    @Inject(method = "advanceTime", at = @At("HEAD"))
    private void smoothAnim$giveBackHeldTicks(long gameTime, CallbackInfoReturnable<Integer> cir) {
        if (smoothAnim$heldTicks > 0) {
            partialTick = smoothAnim$heldResidual + smoothAnim$heldTicks;
            smoothAnim$heldTicks = 0;
        }
    }
}
