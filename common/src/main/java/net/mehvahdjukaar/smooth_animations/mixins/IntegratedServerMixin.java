package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.moonlight.api.platform.network.NetworkHelper;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.mehvahdjukaar.smooth_animations.common.network.ClientBoundNotifyServerTickDoneMessage;
import net.minecraft.client.server.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin {

    //client already ticked whatever was spawned last tick (i.e player arrows but not skelly arros)
    @Inject(method = "tickServer", at = @At("HEAD"))
    private void smoothAnim$clearMidTickSpawns(BooleanSupplier hasTimeLeft,  CallbackInfo ci) {
        SinglePlayerTickManager.clearSpawnedMidEntityTick();
    }

    @Inject(method = "tickServer", at = @At("RETURN"))
    private void smoothAnim$markTickDone(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (SinglePlayerTickManager.isClientDriven()) {
            NetworkHelper.sendToAllClientPlayers(new ClientBoundNotifyServerTickDoneMessage());
        }
    }
}
