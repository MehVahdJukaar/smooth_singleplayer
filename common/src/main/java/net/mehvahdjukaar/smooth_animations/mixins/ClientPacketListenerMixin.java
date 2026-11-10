package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @ModifyArg(method = {"handleTeleportEntity", "handleMoveEntity"}, index = 5,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpTo(DDDFFI)V"))
    private int ss$noLerp(int steps) {
        return SinglePlayerTickManager.isActive(Minecraft.getInstance().getSingleplayerServer()) ? 1 : steps;
    }

    @ModifyArg(method = "handleRotateMob", index = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpHeadTo(FI)V"))
    private int ss$noHeadLerp(int steps) {
        return SinglePlayerTickManager.isActive(Minecraft.getInstance().getSingleplayerServer()) ? 1 : steps;
    }
}
