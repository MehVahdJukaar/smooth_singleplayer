package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.client.renderer.entity.WitherBossRenderer;
import net.minecraft.client.renderer.entity.state.WitherRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBossRenderer.class)
public abstract class WitherBossRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/boss/wither/WitherBoss;Lnet/minecraft/client/renderer/entity/state/WitherRenderState;F)V",
            at = @At("TAIL"))
    private void smoothAnim$lerpSideHeads(WitherBoss wither, WitherRenderState state, float partialTick, CallbackInfo ci) {
        if (!ModConfigs.witherModelFix) return;
        //vanilla keeps old head rots around and then never uses them
        for (int i = 0; i < 2; i++) {
            state.yHeadRots[i] = Mth.rotLerp(partialTick, wither.yRotOHeads[i], wither.yRotHeads[i]);
            state.xHeadRots[i] = Mth.lerp(partialTick, wither.xRotOHeads[i], wither.xRotHeads[i]);
        }
    }
}
