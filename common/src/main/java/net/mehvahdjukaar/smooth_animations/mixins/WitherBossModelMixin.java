package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        if (!ModConfigs.witherModelFix) return;

        //vanilla keeps old head rots around and then never uses them
        float bodyRot = Mth.rotLerp(partialTick, wither.yBodyRotO, wither.yBodyRot);
        for (int i = 0; i < 2; i++) {

            //getHeadX puts head 1 on left side, vanilla draws it on the right one
            ModelPart head = i == 0 ? leftHead : rightHead;
            head.yRot = (Mth.rotLerp(partialTick, wither.yRotOHeads[i], wither.yRotHeads[i]) - bodyRot) * Mth.DEG_TO_RAD;
            head.xRot = Mth.lerp(partialTick, wither.xRotOHeads[i], wither.xRotHeads[i]) * Mth.DEG_TO_RAD;
        }
    }

    //basically vanilla model but side heads turn around their center and spine sits under middle head
    @Inject(method = "createBodyLayer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/model/geom/builders/LayerDefinition;create(Lnet/minecraft/client/model/geom/builders/MeshDefinition;II)Lnet/minecraft/client/model/geom/builders/LayerDefinition;"))
    private static void smoothAnim$centerPivots(CubeDeformation cubeDeformation, CallbackInfoReturnable<LayerDefinition> cir,
                                                @Local PartDefinition partdefinition) {
        if (!ModConfigs.witherModelFix) return;


        float f = 0.20420352F;
        partdefinition.addOrReplaceChild(
            "ribcage",
            CubeListBuilder.create()
                .texOffs(0, 22)
                .addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, cubeDeformation)
                .texOffs(24, 22)
                .addBox(-4.0F, 1.5F, 0.5F, 11.0F, 2.0F, 2.0F, cubeDeformation)
                .texOffs(24, 22)
                .addBox(-4.0F, 4.0F, 0.5F, 11.0F, 2.0F, 2.0F, cubeDeformation)
                .texOffs(24, 22)
                .addBox(-4.0F, 6.5F, 0.5F, 11.0F, 2.0F, 2.0F, cubeDeformation),
            PartPose.offsetAndRotation(-1.5F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F)
        );
        partdefinition.addOrReplaceChild(
            "tail",
            CubeListBuilder.create().texOffs(12, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, cubeDeformation),
            PartPose.offsetAndRotation(-1.5F, 6.9F + Mth.cos(0.20420352F) * 10.0F, -0.5F + Mth.sin(0.20420352F) * 10.0F, 0.83252203F, 0.0F, 0.0F)
        );

        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, cubeDeformation);
        partdefinition.addOrReplaceChild("right_head", cubelistbuilder, PartPose.offset(-9.0F, 3.0F, -1.0F));
        partdefinition.addOrReplaceChild("left_head", cubelistbuilder, PartPose.offset(9.0F, 3.0F, -1.0F));
    }

    @ModifyArg(method = "setupAnim(Lnet/minecraft/world/entity/boss/wither/WitherBoss;FFFFF)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/model/geom/ModelPart;setPos(FFF)V"), index = 0)
    private float smoothAnim$centerTail(float x) {
        return ModConfigs.witherModelFix ? -1.5f : x;
    }

}
