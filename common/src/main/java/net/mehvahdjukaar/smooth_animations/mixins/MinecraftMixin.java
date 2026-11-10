package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.server.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public abstract IntegratedServer getSingleplayerServer();

    @Inject(method = "tick", at = @At("HEAD"))
    private void ss$waitForServerTick(CallbackInfo ci) {
        if (!SinglePlayerTickManager.isTrueSinglePlayer(getSingleplayerServer()) || level == null) {
            //no longer in single player
            SinglePlayerTickManager.disableClientTickDrive();
            return;
        }
        if (!SinglePlayerTickManager.isClientDriven()) {
            SinglePlayerTickManager.activateClientTickDrive();
            return;
        }

        long start = Util.getNanos();
        //ok now we wait for server to do its ticking
        while (!SinglePlayerTickManager.isServerTickDone()) {
            Thread.onSpinWait();
        }
        SmoothAnimations.LOGGER.info("waited {}ms for server", (Util.getNanos() - start) / 1000000f);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void ss$allowNextServerTick(CallbackInfo ci) {
        //tick done. server its your turn
        if (SinglePlayerTickManager.isClientDriven()){
            SinglePlayerTickManager.allowNextServerTick();
        }
    }
}
