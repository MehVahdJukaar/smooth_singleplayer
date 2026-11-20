package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
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
    @Shadow
    @Final
    @Mutable
    private int updateInterval;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void ss$sendEveryTick(CallbackInfo ci) {
        if (SinglePlayerTickManager.isTrueSinglePlayer(level.getServer())) {
            this.updateInterval = 1;
            //OK so this is technically not correct for lan. if one opens lan the shorter interval will remain. maybe a feature?
        }
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
