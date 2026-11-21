package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.world.entity.monster.Slime;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.class)
public abstract class SlimeMixin {

    @Shadow
    public float squish;
    @Shadow
    public float oSquish;

    @Inject(method = "tick", at = @At("HEAD"))
    private void smoothAnim$savePrevSquish(CallbackInfo ci) {
        if (ModConfigs.slimeSquishLerp){
            this.oSquish = squish;
        }
    }

    //vanilla sets oSquish after updating squish so they are always the same... great
    @WrapWithCondition(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/monster/Slime;oSquish:F", opcode = Opcodes.PUTFIELD))
    private boolean smoothAnim$skipSquishOverwrite(Slime instance, float value) {
        return !ModConfigs.slimeSquishLerp;
    }
}
