package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin {

    @Shadow
    private int lerpSteps;

    @Inject(method = "lerpTo", at = @At("TAIL"))
    private void smoothAnim$dontAddLerpStepsFFS(double x, double y, double z, float yRot, float xRot, int steps, CallbackInfo ci) {
        //mc adds 2 here for whatever reason
        if (SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer())) {
            this.lerpSteps = steps;
        }
    }
}
