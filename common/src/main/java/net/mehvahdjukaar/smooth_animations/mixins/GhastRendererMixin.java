package net.mehvahdjukaar.smooth_animations.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.common.SwellingGhast;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GhastRenderer.class)
public abstract class GhastRendererMixin {

    @Inject(method = "scale(Lnet/minecraft/world/entity/monster/Ghast;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At("TAIL"))
    private void ss$fixMC165038(Ghast ghast, PoseStack poseStack, float partialTick, CallbackInfo ci) {
        if (ModConfigs.ghastSwell == ModConfigs.GhastSwell.OFF) return;

        float charge = ((SwellingGhast) ghast).smoothSP$getChargeAmount(partialTick);
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
        if (ModConfigs.ghastSwell == ModConfigs.GhastSwell.SWELL) {
            //succ
            var5 = var6;
        }
        poseStack.scale(var6 / 4.5f, var5 / 4.5f, var6 / 4.5f);
    }
}
