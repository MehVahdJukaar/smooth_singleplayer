package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.smooth_animations.common.LandingFallingBlock;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FallingBlockRenderer.class)
public abstract class FallingBlockRendererMixin {

    @ModifyExpressionValue(method = "shouldRender(Lnet/minecraft/world/entity/item/FallingBlockEntity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z",
            at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState ss$keepDrawingWhileLanding(BlockState original, @Local(argsOnly = true) FallingBlockEntity entity) {
        //hijacks so it thinks we pass through air
        return ((LandingFallingBlock) entity).smoothSP$isLanding() ? Blocks.AIR.defaultBlockState() : original;
    }


    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/item/FallingBlockEntity;Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;F)V",
            at = @At("TAIL"))
    private void smoothAnim$stopOnImpact(FallingBlockEntity entity, FallingBlockRenderState state, float partialTicks, CallbackInfo ci) {
        LandingFallingBlock landing = (LandingFallingBlock) entity;
        if (landing.smoothSP$isLanding()) {
            state.y += landing.smoothSP$getLandingRenderYOffset(partialTicks);
        }
    }
}
