package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.client.SmoothAnimationsClient;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InterpolationHandler.class)
public abstract class InterpolationHandlerMixin {

    @Shadow
    @Final
    private Entity entity;

    @Shadow
    public abstract Vec3 position();

    @ModifyExpressionValue(method = "interpolateTo", at = @At(value = "FIELD",
            target = "Lnet/minecraft/world/entity/InterpolationHandler;interpolationSteps:I"))
    private int smoothAnim$stopTPLerp(int steps) {
        //displays seem to get their steps from teleport_duration, leave those to the map makers
        if (entity instanceof Display) return steps;
        if (!ModConfigs.snapEntityPosition) return steps;
        if (!SmoothAnimationsClient.isTickLockedToServer()) {
            return steps;
        }
        //mobs keep lerp for yrot, setPos below does their position
        return entity instanceof LivingEntity ? steps : 1;
    }

    @WrapOperation(method = "interpolate", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;setPos(Lnet/minecraft/world/phys/Vec3;)V"))
    private void smoothAnim$snapPositionToServer(Entity instance, Vec3 pos, Operation<Void> original) {
        if (ModConfigs.snapEntityPosition && SmoothAnimationsClient.isTickLockedToServer()) {
            //snap to position instantly  while we let rot interpolate (as if interpolation step was 1 but only for pos)
            original.call(instance, this.position());
        } else {
            original.call(instance, pos);
        }
    }
}
