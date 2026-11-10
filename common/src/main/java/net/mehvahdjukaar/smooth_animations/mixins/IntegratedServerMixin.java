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

    @Inject(method = "tickServer", at = @At("RETURN"))
    private void ss$markTickDone(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (SinglePlayerTickManager.isClientDriven()) {
            NetworkHelper.sendToAllClientPlayers(new ClientBoundNotifyServerTickDoneMessage());
        }
    }
}
