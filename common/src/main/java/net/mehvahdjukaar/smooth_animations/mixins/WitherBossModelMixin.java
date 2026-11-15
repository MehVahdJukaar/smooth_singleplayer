package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBossModel.class)
public abstract class WitherBossModelMixin {

    @Shadow
    @Final
    private ModelPart rightHead;
    @Shadow
    @Final
    private ModelPart leftHead;

    @Inject(method = "prepareMobModel(Lnet/minecraft/world/entity/boss/wither/WitherBoss;FFF)V", at = @At("TAIL"))
    private void smootAnim$lerpSideHeads(WitherBoss wither, float limbSwing, float limbSwingAmount, float partialTick, CallbackInfo ci) {
        if (!ModConfigs.entityRotationFix) return;

        //vanilla keeps old head rots around and then never uses them
        float bodyRot = Mth.rotLerp(partialTick, wither.yBodyRotO, wither.yBodyRot);
        for (int i = 0; i < 2; i++) {

            ModelPart head = i == 0 ? rightHead : leftHead;
            head.yRot = (Mth.rotLerp(partialTick, wither.yRotOHeads[i], wither.yRotHeads[i]) - bodyRot) * Mth.DEG_TO_RAD;
            head.xRot = Mth.lerp(partialTick, wither.xRotOHeads[i], wither.xRotHeads[i]) * Mth.DEG_TO_RAD;
        }
    }
}
