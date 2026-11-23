package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.client.SmoothAnimationsClient;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    protected double lerpX;
    @Shadow
    protected double lerpY;
    @Shadow
    protected double lerpZ;

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setPos(DDD)V"))
    private void smoothAnim$snapPositionToServer(LivingEntity instance, double x, double y, double z, Operation<Void> original) {
        if (ModConfigs.snapEntityPosition && SmoothAnimationsClient.isTickLockedToServer()) {
            //snap to position instantly  while we let rot interpolate (as if interpolation step was 1 but only for pos)
            original.call(instance, lerpX, lerpY, lerpZ);
        } else {
            original.call(instance, x, y, z);
        }
    }
}
