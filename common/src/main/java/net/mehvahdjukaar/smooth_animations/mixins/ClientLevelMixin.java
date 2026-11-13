package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.common.LandingFallingBlock;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "removeEntity", at = @At("HEAD"), cancellable = true)
    private void smoothAnim$letFallingBlockLand(int entityId, Entity.RemovalReason reason, CallbackInfo ci) {
        if (ModConfigs.fallingBlockLanding && (((ClientLevel) (Object) this).getEntity(entityId) instanceof FallingBlockEntity f)
                && ((LandingFallingBlock) f).smoothSP$startLanding()) {
            ci.cancel();
        }
    }

    @Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
    private void smoothAnim$holdNewEntity(Entity p_entity, CallbackInfo ci) {
        //on this is complicated. basically SOME entity spawned at right times get spawned on server AND ticked
        //others, like in mobs AI are spawned AFTER tick has happened.
        //all client entities must be at most 1 tick behind the server as thats the time it takes for a packet to arrive
        //hereis order:
        //1. block ticks & handle packets
        //2. send packets
        //3. server tick (move)
        //sekelton arrow is spawned AFTER ticks (in server tick). player arrow generated in 1 instead so it ticks and moves rightafter.
        //this means on tick 2 BOTH arrows are at pos 0 BUT server is ahead on player (good) but NOT ahead on skelly.
        //so later on the server send sync packet and arrow is snapped back...
        if (SinglePlayerTickManager.consumeHeldEntityTick(p_entity)){
         ci.cancel();
        }
    }
}
