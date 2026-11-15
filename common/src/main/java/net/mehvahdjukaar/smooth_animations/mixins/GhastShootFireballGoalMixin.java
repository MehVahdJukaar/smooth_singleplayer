package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.common.network.ClientBoundGhastChargingMessage;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.entity.monster.Ghast$GhastShootFireballGoal")
public abstract class GhastShootFireballGoalMixin {
    @Shadow
    @Final
    private Ghast ghast;
    @Shadow
    public int chargeTime;

    @Unique
    private boolean ss$rising;

    @Inject(method = "tick", at = @At("HEAD"))
    private void ss$storeChargeTime(CallbackInfo ci, @Share("chargeBefore") LocalIntRef chargeBefore) {
        chargeBefore.set(chargeTime);
    }
    @Inject(method = "tick", at = @At("TAIL"))
    private void ss$sendChargeChanges(CallbackInfo ci, @Share("chargeBefore") LocalIntRef chargeBefore) {
        boolean rising = chargeTime > 0 && chargeTime > chargeBefore.get();
        if (rising != ss$rising) {
            ss$sendCharging(rising);
        }
    }



    @Unique
    private static final double ss$FIREBALL_Y_AT_MOUTH = 0.1275;

    @ModifyArg(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/LargeFireball;setPos(DDD)V"), index = 1)
    private double smoothAnim$shootFromMouth(double y) {
        if (!ModConfigs.ghastMouthFireball) return y;
        return ghast.getY() + ss$FIREBALL_Y_AT_MOUTH;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V"), index = 1)
    private double smoothAnim$aimFromMouth(double dy) {
        if (!ModConfigs.ghastMouthFireball) return dy;
        return dy + ghast.getY(0.5) + 0.5 - (ghast.getY() + ss$FIREBALL_Y_AT_MOUTH);
    }


    @Inject(method = "stop", at = @At("HEAD"))
    private void ss$stopCharge(CallbackInfo ci) {
        if (ss$rising) {
            ss$sendCharging(false);
        }
    }

    //unlike vanilla fires right as it opens its mouth*
    @Unique
    private void ss$sendCharging(boolean rising) {
        ss$rising = rising;
        ((ServerLevel) ghast.level()).getChunkSource().broadcast(ghast,
                new ClientboundCustomPayloadPacket(new ClientBoundGhastChargingMessage(ghast.getId(), rising)));
    }

}
