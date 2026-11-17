package net.mehvahdjukaar.smooth_animations.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.smooth_animations.client.ChargingGhastState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    //GhastRenderer doesnt override scale so we hook it here
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    private void ss$fixMC165038(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                                CameraRenderState camera, CallbackInfo ci) {
        if (!(state instanceof ChargingGhastState ghast)) return;
        float charge = ghast.smoothSP$getCharge();
        if (charge == 0) {
            return;
        }

        //straight out just directly copied from 1.2.5 RenderGhast
        float var4 = charge;
        if (var4 < 0.0F) {
            var4 = 0.0F;
        }
        var4 = 1.0F / (var4 * var4 * var4 * var4 * var4 * 2.0F + 1.0F);
        float var5 = (8.0F + var4) / 2.0F;
        float var6 = (8.0F + 1.0F / var4) / 2.0F;
        poseStack.scale(var6 / 4.5f, var5 / 4.5f, var6 / 4.5f);
    }
}
