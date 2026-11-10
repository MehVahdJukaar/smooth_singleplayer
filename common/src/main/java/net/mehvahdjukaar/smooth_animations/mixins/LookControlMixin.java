package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.LookControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LookControl.class)
public abstract class LookControlMixin {

    @ModifyVariable(method = "rotateTowards", at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private float smooth_sp$slowDownNearTarget(float maxDelta, @Local(argsOnly = true, ordinal = 0) float from,
                                        @Local(argsOnly = true, ordinal = 1) float to) {
        //attepts making head animation less harsh. make configrable
        float remaining = Math.abs(Mth.degreesDifference(from, to));
        return Math.clamp(remaining * 0.5f, Math.min(1, maxDelta), maxDelta);
    }
}
