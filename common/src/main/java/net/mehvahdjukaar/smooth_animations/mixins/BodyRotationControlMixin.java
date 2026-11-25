package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BodyRotationControl.class)
public abstract class BodyRotationControlMixin {

    @Shadow
    @Final
    private Mob mob;

    @Shadow
    protected abstract void rotateBodyIfNecessary();
    @Shadow
    protected abstract boolean notCarryingMobPassengers();
    @Shadow
    protected abstract boolean isMoving();

    @Inject(method = "clientTick", at = @At("TAIL"))
    private void smoothSP$keepBodyInReachEveryTick(CallbackInfo ci) {
        //so vanilla only drags the body when the head rotates 15 deg so the body rotation is jagged as heck

        if (ModConfigs.entityRotationFix && notCarryingMobPassengers()) {
            rotateBodyIfNecessary();
        }
        //walking starts from yRot. It can happen that it might stll point to where it last walked to so a snap would occur
        if (ModConfigs.idleYawFollowsBody && !isMoving() && !mob.isVehicle()) {
            mob.setYRot(mob.yBodyRot);
        }
    }

    @ModifyExpressionValue(method = "rotateHeadTowardsFront", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Mth;clamp(FFF)F"))
    private float smoothSp$easeBodyCatchUp(float progress) {
        if (!ModConfigs.entityRotationFix) {
            return progress;
        }
        //smoothstep
        return  progress * progress * (3 - 2 * progress);
    }
}
