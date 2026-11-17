package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.client.renderer.entity.DrownedRenderer;
import net.minecraft.world.entity.monster.Drowned;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = DrownedRenderer.class, priority = 1001)
public abstract class DrownedRendererMixin {

    @ModifyExpressionValue(method = "setupRotations(Lnet/minecraft/world/entity/monster/Drowned;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Drowned;getXRot()F"))
    private float smoothSP$lerpSwimPitch(float pitch, Drowned drowned, PoseStack poseStack, float bob,
                                         float yBodyRot, float partialTick) {
        if (!ModConfigs.pitchLerpFix)return pitch;

        return drowned.getViewXRot(partialTick);
    }


}
