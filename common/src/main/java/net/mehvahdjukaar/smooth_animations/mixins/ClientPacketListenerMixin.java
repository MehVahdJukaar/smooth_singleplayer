package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @ModifyArg(method = {"handleTeleportEntity", "handleMoveEntity"}, index = 5,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpTo(DDDFFIZ)V"))
    private int smoothAnim$stopTPLerp(int steps, @Local Entity entity) {
        if (!ModConfigs.snapEntityPosition) return steps;
        if (!SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer())) {
            return steps;
        }
        //mobs keep lerp for yrot, LivingEntityMixin does their position. kinda sketchy since a non living might need interpolation too? check again
        return entity instanceof LivingEntity ? steps : 1;
    }
}
