package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BodyRotationControl.class)
public abstract class BodyRotationControlMixin {

    @Shadow
    protected abstract void rotateBodyIfNecessary();
    @Shadow
    protected abstract boolean notCarryingMobPassengers();

    @Inject(method = "clientTick", at = @At("TAIL"))
    private void smoothSP$keepBodyInReachEveryTick(CallbackInfo ci) {
        //so vanilla only drags the body when the head rotates 15 deg so the body rotation is jagged as heck

        if (notCarryingMobPassengers()) {
            rotateBodyIfNecessary();
        }
    }

    @ModifyExpressionValue(method = "rotateHeadTowardsFront", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Mth;clamp(FFF)F"))
    private float smoothSp$easeBodyCatchUp(float progress) {
        //some janky smooth funciton. check
        return progress * progress * (3 - 2 * progress);
    }
}
