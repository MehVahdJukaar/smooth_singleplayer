package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.entity.ai.control.LookControl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LookControl.class)
public abstract class LookControlMixin implements Control {

    @Override
    public float rotateTowards(float from, float to, float maxDelta) {
        if (ModConfigs.entityRotationFix) {
            //attepts making head animation less harsh. make configrable
            float remaining = Math.abs(Mth.degreesDifference(from, to));

            maxDelta = Math.clamp(remaining * 0.5f, Math.min(1, maxDelta), maxDelta);
        }
        return Control.super.rotateTowards(from, to, maxDelta);
    }
}
