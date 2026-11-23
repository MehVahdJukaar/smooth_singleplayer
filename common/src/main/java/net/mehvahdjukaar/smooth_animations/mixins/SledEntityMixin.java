package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.LerpedOnClient;
import net.mehvahdjukaar.smooth_animations.client.SmoothAnimationsClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//snowy spirit
@Pseudo
@Mixin(targets = "net.mehvahdjukaar.snowyspirit.common.entity.SledEntity")
public abstract class SledEntityMixin implements LerpedOnClient {

    @Shadow
    private int lerpSteps;

    @Inject(method = {"lerpTo", "method_5759"}, at = @At("TAIL"))
    private void smoothAnim$useGivenLerpSteps(double x, double y, double z, float yRot, float xRot, int steps, boolean teleport, CallbackInfo ci) {
        if (SmoothAnimationsClient.isTickLockedToServer()) {
            lerpSteps = steps;
        }
    }
}
