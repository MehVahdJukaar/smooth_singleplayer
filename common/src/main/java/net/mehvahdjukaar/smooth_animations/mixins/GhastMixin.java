package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.SwellingGhast;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Ghast.class)
public abstract class GhastMixin extends FlyingMob implements SwellingGhast {

    @Unique
    private int ss$chargeStartTick = -1;
    @Shadow
    public abstract boolean isCharging();

    protected GhastMixin(EntityType<? extends FlyingMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void smoothSP$onGhastSyncedDataUpdate(EntityDataAccessor<?> key) {
        if(key == Ghast.DATA_IS_CHARGING)
        //fires for all synced data updates
        if (!isCharging()) {
            ss$chargeStartTick = -1;
        } else if (ss$chargeStartTick == -1) {
            ss$chargeStartTick = tickCount;
        }
    }

    @Override
    public float smoothSP$getChargeAmount(float partialTick) {
        if (ss$chargeStartTick == -1) {
            return 0;
        }
        //so the goal syncs the data at 10 and should shoot at 20. Here we just count hoping they match.
        float chargeTime = (10 + tickCount - ss$chargeStartTick ) + partialTick;
        return Math.min(chargeTime/20f, 1);
    }
}
