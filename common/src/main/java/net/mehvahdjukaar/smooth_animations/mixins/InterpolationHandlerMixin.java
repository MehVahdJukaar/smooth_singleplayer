package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InterpolationHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InterpolationHandler.class)
public abstract class InterpolationHandlerMixin {

    @Shadow
    @Final
    private Entity entity;

    @ModifyExpressionValue(method = "interpolateTo", at = @At(value = "FIELD",
            target = "Lnet/minecraft/world/entity/InterpolationHandler;interpolationSteps:I"))
    private int smoothAnim$stopTPLerp(int steps) {
        //displays seem to get their steps from teleport_duration, leave those to the map makers
        if (entity instanceof Display) return steps;

        return SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer()) ? 1 : steps;
    }
}
