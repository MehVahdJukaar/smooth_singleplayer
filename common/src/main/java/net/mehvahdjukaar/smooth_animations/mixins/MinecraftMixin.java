package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.client.IJitterFixTimer;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
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
    private Timer timer;

    @Shadow
    @Nullable
    public abstract IntegratedServer getSingleplayerServer();

    @Inject(method = "tick", at = @At("HEAD"))
    private void ss$waitForServerTick(CallbackInfo ci) {
        IntegratedServer server = getSingleplayerServer();
        boolean canSync = SinglePlayerTickManager.isTrueSinglePlayer(server) && level != null && player != null;
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
        long giveUpAt = start + SinglePlayerTickManager.MAX_LOCK_WAIT_NANO;
        var mc = ((Minecraft) (Object) this);
        //block until server is done
        mc.managedBlock(() -> !SinglePlayerTickManager.shouldWaitForServer() || System.nanoTime() > giveUpAt);

        //SmoothAnimations.LOGGER.debug("waited {}ms for server", (System.nanoTime() - start) / 1000000f);
    }

    // runs after runAllTasks. that means the tick done packet is already handled here
    @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;tick()V"))
    private void smoothAnim$holdTickUntilServerDone(Minecraft mc, Operation<Void> tick) {
        boolean serverStillTicking = level != null && SinglePlayerTickManager.isClientDriven() && SinglePlayerTickManager.shouldWaitForServer();
        //under 20 fps a frame is longer than a tick so holding would just slow the game down. . thast why we block in tick() instead
        if (serverStillTicking && timer.tickDelta < 1) {
            ((IJitterFixTimer) timer).smoothAnim$freezeTick();
        } else {
            tick.call(mc);
        }
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
        if (ModConfigs.frameTimeCatchUp) {
            ((IJitterFixTimer) timer).smoothAnim$catchUpPartialTick(Util.getMillis());
        }
    }
}
