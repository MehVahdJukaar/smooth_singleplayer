package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.client.SmoothAnimationsClient;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @WrapOperation(method = "lerpPositionAndRotationStep", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;setPos(DDD)V"))
    private void smoothAnim$snapPositionToServer(Entity instance, double x, double y, double z, Operation<Void> original,
                                         @Local(argsOnly = true, ordinal = 0) double targetX,
                                         @Local(argsOnly = true, ordinal = 1) double targetY,
                                         @Local(argsOnly = true, ordinal = 2) double targetZ) {
        if (ModConfigs.snapEntityPosition && SmoothAnimationsClient.isTickLockedToServer()) {
            //snap to position instantly  while we let rot interpolate (as if interpolation step was 1 but only for pos)
            original.call(instance, targetX, targetY, targetZ);
        } else {
            original.call(instance, x, y, z);
        }
    }
}