package net.mehvahdjukaar.smooth_animations.common;

import net.minecraft.network.syncher.EntityDataAccessor;

public interface SwellingGhast {


    void smoothSP$onGhastSyncedDataUpdate(EntityDataAccessor<?> key);


    float smoothSP$getChargeAmount(float partialTick);

}
