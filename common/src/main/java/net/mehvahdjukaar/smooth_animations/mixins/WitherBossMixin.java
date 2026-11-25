package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.client.SmoothAnimationsClient;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {

    @WrapWithCondition(method = "aiStep", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;setYRot(F)V"))
    private boolean smoothSP$serverYawOnlyStopShake(WitherBoss wither, float yRot) {
        if (!ModConfigs.witherYawFix || !wither.level().isClientSide) return true;
        //on client yaw is set in 2 places. creating wobble. norally not an issue as server overrides this every tick in SP but on lan thats not the case so we do this
        return SmoothAnimationsClient.isTickLockedToServer();
    }
}
