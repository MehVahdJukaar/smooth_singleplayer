package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.LerpedOnClient;
import net.mehvahdjukaar.smooth_animations.client.SmoothAnimationsClient;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class BoatMixin implements LerpedOnClient {

    @Shadow
    private int lerpSteps;

    @Inject(method = "lerpTo", at = @At("TAIL"))
    private void smoothAnim$useGivenLerpStepsFFS(double x, double y, double z, float yRot, float xRot, int steps, boolean teleport, CallbackInfo ci) {
        if (SmoothAnimationsClient.isTickLockedToServer()) {
            lerpSteps = steps;
        }
    }
}
