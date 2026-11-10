package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> {

    @Shadow
    private long nextTickTimeNanos;

    protected MinecraftServerMixin(String name) {
        super(name);
    }

    //TODO: not idea, later change to a better wrap better mixin type.
    @Inject(method = "waitUntilNextTick", at = @At("HEAD"), cancellable = true)
    private void ss$waitForClientTick(CallbackInfo ci) {
        if (!SinglePlayerTickManager.isClientDriven()) return;

        long giveUpAt = Util.getNanos() + SinglePlayerTickManager.WAIT_LIMIT_NANOS;
        this.runAllTasks();
        //same deal as the client
        this.managedBlock(() -> SinglePlayerTickManager.tryStartServerTick() || Util.getNanos() > giveUpAt);
        //the client sets the pace now. hyjacks this otherwise server thinks its behind and spams cant keep up..
        this.nextTickTimeNanos = Util.getNanos();
        ci.cancel();
    }
}
