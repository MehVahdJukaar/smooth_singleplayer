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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

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

        long giveUpAt = System.nanoTime() + SinglePlayerTickManager.WAIT_LIMIT_NANOS;
        this.runAllTasks();
        //same deal as the client
        this.managedBlock(() -> SinglePlayerTickManager.tryStartServerTick() || System.nanoTime() > giveUpAt);
        //the client sets the pace now. hyjacks this otherwise server thinks its behind and spams cant keep up..
        this.nextTickTimeNanos = Util.getNanos();
        ci.cancel();
    }

    @ModifyArg(method = "waitUntilNextTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;managedBlock(Ljava/util/function/BooleanSupplier;)V"))
    private BooleanSupplier smoothAnim$stopWaitingWhenClientTakesOver(BooleanSupplier isDone) {
        //handles takeover when its waiting
        return () -> isDone.getAsBoolean() || SinglePlayerTickManager.isClientDriven();
    }
}
