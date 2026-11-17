package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.client.ChargingGhastState;
import net.mehvahdjukaar.smooth_animations.common.SwellingGhast;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.client.renderer.entity.state.GhastRenderState;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GhastRenderer.class)
public abstract class GhastRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/Ghast;Lnet/minecraft/client/renderer/entity/state/GhastRenderState;F)V",
            at = @At("TAIL"))
    private void ss$extractCharge(Ghast ghast, GhastRenderState state, float partialTick, CallbackInfo ci) {
        float charge = ModConfigs.ghastSwell != ModConfigs.GhastSwell.OFF ? ((SwellingGhast) ghast).smoothSP$getChargeAmount(partialTick) : 0;
        ((ChargingGhastState) state).smoothSP$setCharge(charge);
    }
}
