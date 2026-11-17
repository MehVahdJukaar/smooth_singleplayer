package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

    @Shadow
    @Final
    private ServerLevel level;
    @Shadow
    @Final
    private Entity entity;

    @ModifyExpressionValue(method = "sendChanges", at = @At(value = "FIELD",
            target = "Lnet/minecraft/server/level/ServerEntity;updateInterval:I"))
    private int ss$sendEveryTick(int updateInterval) {
        return SinglePlayerTickManager.isTrueSinglePlayer(level.getServer()) ? 1 : updateInterval;
    }

    @Inject(method = "sendChanges", at = @At("HEAD"), cancellable = true)
    private void smoothAnim$sendAfterEntityTick(CallbackInfo ci) {
        if (SinglePlayerTickManager.isSendingDeferredChanges()) {
            if (entity.isRemoved()) {
                ci.cancel();
            }
            return;
        }

        if (SinglePlayerTickManager.shouldDeferSetChanges((ServerEntity) (Object) this, entity)) {
            ci.cancel();
        }
    }
}
