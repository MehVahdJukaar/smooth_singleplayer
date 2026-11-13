package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.SwellingGhast;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    //yeah finaly doing this correctly. not in ghast as it would be an override. not stackable
    @Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V", at = @At("HEAD"))
    private void smoothAnim$ghastSwellThing(EntityDataAccessor<?> key, CallbackInfo ci) {
        if (this instanceof SwellingGhast g) {
            g.smoothSP$onGhastSyncedDataUpdate(key);
        }
    }
}