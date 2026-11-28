package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Shadow
    protected abstract void sendDirtyEntityData();

    @Unique
    private boolean smoothAnim$sentDataBeforeEntityTick;
    @Unique
    private boolean smoothAnim$heldDataLastTick;

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
            //mcreator mobs set their animation on hurt and clear it in their own tick.
            //because of course they are...
            // send it before that clears it
            //also held data from last tick doesnt count or a mob that writes data every tick never gets out of the hold
            smoothAnim$sentDataBeforeEntityTick = !smoothAnim$heldDataLastTick && entity.getEntityData().isDirty();
            smoothAnim$heldDataLastTick = false;
            this.sendDirtyEntityData();
            ci.cancel();
        }
    }

    //the reset has to land a tick later or the client never ticks with the animation set
    @WrapWithCondition(method = "sendChanges", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerEntity;sendDirtyEntityData()V"))
    private boolean smoothAnim$impedeAlreadySentData(ServerEntity instance) {
        if (SinglePlayerTickManager.isSendingDeferredChanges() && smoothAnim$sentDataBeforeEntityTick) {
            //we already sent
            return false;
        }
        return true;
    }

    @Inject(method = "sendChanges", at = @At("RETURN"))
    private void smoothAnim$clearEarlyData(CallbackInfo ci) {
        if (SinglePlayerTickManager.isSendingDeferredChanges()){
            smoothAnim$heldDataLastTick = smoothAnim$sentDataBeforeEntityTick;
            smoothAnim$sentDataBeforeEntityTick = false;
        }
    }
}
