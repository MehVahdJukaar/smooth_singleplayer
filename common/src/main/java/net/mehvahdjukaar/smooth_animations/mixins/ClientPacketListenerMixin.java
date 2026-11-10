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
    private int smoothAnim$stopTPLerp(int steps) {
        return SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer()) ? 1 : steps;
    }

    @ModifyArg(method = "handleRotateMob", index = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpHeadTo(FI)V"))
    private int smoothAnim$stopHeadLerp(int steps) {
        return SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer()) ? 1 : steps;
    }
}
