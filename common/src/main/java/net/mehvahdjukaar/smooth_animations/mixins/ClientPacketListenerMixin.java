package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.smooth_animations.common.FirstServerUpdateAware;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @WrapOperation(method = {"handleTeleportEntity", "handleMoveEntity"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpTo(DDDFFI)V"))
    private void smoothAnim$stopTPLerp(Entity entity, double x, double y, double z, float yRot, float xRot, int steps,
                                       Operation<Void> original) {
        ((FirstServerUpdateAware) entity).smoothAnim$markServerUpdated();

        boolean sp = SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer());
        original.call(entity, x, y, z, yRot, xRot, sp ? 1 : steps);
    }

    @ModifyArg(method = "handleRotateMob", index = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpHeadTo(FI)V"))
    private int smoothAnim$stopHeadLerp(int steps) {
        return SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer()) ? 1 : steps;
    }
}
