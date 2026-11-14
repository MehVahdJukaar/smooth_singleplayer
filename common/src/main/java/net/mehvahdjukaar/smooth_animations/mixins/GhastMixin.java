package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.mehvahdjukaar.smooth_animations.common.SwellingGhast;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Ghast.class)
public abstract class GhastMixin extends FlyingMob implements SwellingGhast {

    //forn 0 to 20
    @Unique
    private int ss$chargeTime;
    @Unique
    private int ss$prevChargeTime;
    //if our packet is being used
    @Unique
    private boolean ss$chargingFromServer;
    @Shadow
    public abstract boolean isCharging();

    protected GhastMixin(EntityType<? extends FlyingMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        //alternative could also be react to synced data update. this is more resilient
        boolean clientDriven = SinglePlayerTickManager.isClientDriven();
        ss$prevChargeTime = ss$chargeTime;
        if (ss$chargingFromServer || isCharging()) {
            //so the goal syncs the data at 1 and should shoot at 20.
            //Since client is 1 tick behind we start from 12 to peak on the shot.
            // But wait, actually here we encounter that other projectile bug that makes fireball wait 1 tick so we have to wait too

            // our packet comes at 1 the flag only at 11 . 2 possible stsarts
            int start = (ss$chargingFromServer ? 1 : 11) + (clientDriven ? 0 : 1);
            ss$chargeTime = Mth.clamp(ss$chargeTime + 1, start, 20);
        } else if (clientDriven && ss$chargeTime == 19) {
            ss$chargeTime = 20;
        } else {
            ss$chargeTime = Math.max(ss$chargeTime - 5, 0);
        }
    }

    @Override
    public void smoothSP$setChargingFromServer(boolean charging) {
        ss$chargingFromServer = charging;
    }


    @Override
    public float smoothSP$getChargeAmount(float partialTick) {
        return Mth.lerp(partialTick, ss$prevChargeTime, ss$chargeTime) / 20f;
    }
}
