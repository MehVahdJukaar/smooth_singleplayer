package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.smooth_animations.common.network.LandingFallingBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FallingBlockRenderer.class)
public abstract class FallingBlockRendererMixin {

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState ss$keepDrawingWhileLanding(BlockState original, @Local(argsOnly = true) FallingBlockEntity entity) {
        //hijacks so it thinks we pass through air
        return ((LandingFallingBlock) entity).smoothSP$isLanding() ? Blocks.AIR.defaultBlockState() : original;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"))
    private void ss$stopOnImpact(FallingBlockEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                                 MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        LandingFallingBlock landing = (LandingFallingBlock) entity;
        if (landing.smoothSP$isLanding()) {
            poseStack.translate(0, landing.smoothSP$getLandingRenderYOffset(partialTicks), 0);
        }
    }
}