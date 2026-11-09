package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.network.LandingFallingBlock;
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
    private void ss$letFallingBlockLand(int entityId, Entity.RemovalReason reason, CallbackInfo ci) {
        if (((ClientLevel) (Object) this).getEntity(entityId) instanceof FallingBlockEntity f
                && ((LandingFallingBlock) f).smoothSP$startLanding()) {
            ci.cancel();
        }
    }
}
