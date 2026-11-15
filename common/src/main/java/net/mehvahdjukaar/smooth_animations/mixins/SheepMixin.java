package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @Shadow
    private int eatAnimationTick ;
    @Shadow
    public abstract float getHeadEatPositionScale(float partialTick);

    @ModifyReturnValue(method = "getHeadEatAngleScale", at = @At("RETURN"))
    private float smoothAnim$lerpHeadPitch(float angle, float partialTick) {
        if (!ModConfigs.pitchLerpFix) return angle;

        //stolen from youve goat to be kidding me
        if (eatAnimationTick > 4 && eatAnimationTick <= 36) return angle;
        //the head goes down over 4 ticks but vanilla snaps its angle in one
        float pitch = ((Sheep) (Object) this).getViewXRot(partialTick) * Mth.DEG_TO_RAD;
        return Mth.lerp(getHeadEatPositionScale(partialTick), pitch, Mth.PI /5);
    }

}
