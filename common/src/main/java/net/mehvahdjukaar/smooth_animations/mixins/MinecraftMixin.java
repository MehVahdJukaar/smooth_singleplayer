package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.mehvahdjukaar.smooth_animations.client.FrameTimeCatchUp;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
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
    public LocalPlayer player;
    @Shadow
    @Final
    private DeltaTracker.Timer timer;

    @Shadow
    @Nullable
    public abstract IntegratedServer getSingleplayerServer();

    @Inject(method = "tick", at = @At("HEAD"))
    private void ss$waitForServerTick(CallbackInfo ci) {
        IntegratedServer server = getSingleplayerServer();
        boolean canSync = SinglePlayerTickManager.isTrueSinglePlayer(server) && level != null && player != null
                && !server.tickRateManager().isSprinting();
        if (!canSync) {
            //no longer in single player
            SinglePlayerTickManager.disableClientTickDrive();
            return;
        }
        if (!SinglePlayerTickManager.isClientDriven()) {
            SinglePlayerTickManager.activateClientTickDrive();
            return;
        }

        long start = System.nanoTime();
        //ok now we wait for server to do its ticking
        long giveUpAt = start + SinglePlayerTickManager.WAIT_LIMIT_NANOS;
        var mc = ((Minecraft) (Object) this);
        //block until server is done
        mc.managedBlock(() -> SinglePlayerTickManager.hasReceivedAllowedTicks() || System.nanoTime() > giveUpAt);

        SmoothAnimations.LOGGER.info("waited {}ms for server", (System.nanoTime() - start) / 1000000f);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void ss$allowNextServerTick(CallbackInfo ci) {
        //tick done. server its your turn
        if (SinglePlayerTickManager.isClientDriven()){
            SinglePlayerTickManager.allowNextServerTick();
        }
    }

    //partial tick is ocomputed right before the tick runs, so frames with a tick draw stuff a few ms late
    @Inject(method = "tick", at = @At("TAIL"))
    private void smoothAnim$catchUpFrameTime(CallbackInfo ci) {
        ((FrameTimeCatchUp) timer).smoothAnim$catchUpPartialTick(Util.getMillis());
    }
}
