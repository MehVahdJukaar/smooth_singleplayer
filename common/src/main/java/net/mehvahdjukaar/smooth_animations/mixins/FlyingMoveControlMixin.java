package net.mehvahdjukaar.smooth_animations.mixins;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlyingMoveControl.class)
public abstract class FlyingMoveControlMixin {

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;setYRot(F)V"))
    private boolean smoothSP$noMoreWitherYawSpasm(Mob mob, float yRot) {
        if(!ModConfigs.witherYawFix)return true;

        //ok so here whats happens is that unlike other mobs wither aiStep tries to face the WHOLE body toward the final TARGET
        //this fights the move controller that does the same but toward the node. these 2 might not match
        return !(mob instanceof WitherBoss);
    }
}
