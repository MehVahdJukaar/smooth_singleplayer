package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
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
