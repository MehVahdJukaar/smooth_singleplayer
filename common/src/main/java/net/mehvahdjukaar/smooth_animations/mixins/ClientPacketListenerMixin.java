package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.client.SmoothAnimationsClient;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.damagesource.DamageSource;
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
        if (!SmoothAnimationsClient.isTickLockedToServer()) {
            return steps;
        }
        //mobs keep lerp for yrot, LivingEntityMixin does their position. kinda sketchy since a non living might need interpolation too? check again
        return entity instanceof LivingEntity ? steps : 1;
    }

    //what happens here:
    //this assumes 0 tick is turned on. won't bother try to do without as what this fixes is a tiny discrepancy.
    //TICK 0:
    //when a client attack its authoritative, it sends its attack packet to server,
    //client done, time for server.
    //server ticks after client it gets the packet and immediately sends the hurt packet we receive here
    //server applies knockback, new mob pos is set
    //server sends out positon packets (after move, here, since 0 tick config is on) containg new moved pos.
    //server finish ticking
    //between ticks packets are received, the new position is gotten and set as interpolationt target
    //client has received the new mob pos with applied knockback, stored in interpolationHandler, its pos are still xo=0, x=0
    //PROBLEM: packet plays sound right away (end of client tick) & flicks the hurt flag so in between this and next client move we see the mob flashing red BEFORE it even moved
    //TICK 1:
    //client sets xo=x, x = interpolation target, we have the new pos we are moving & rendering toward. Similar happens for hand swing animation

    //solution: flick the red flag & play hurt sound on next client tick when movement is visible


    @WrapOperation(method = "handleHurtAnimation", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;animateHurt(F)V"))
    private void smoothAnim$holdHurtAnimation(Entity entity, float direction, Operation<Void> original) {
        if (SmoothAnimationsClient.shouldHoldHurtEffects(entity)) {
            SmoothAnimationsClient.holdHurtEffect(() -> original.call(entity, direction));
        } else {
            original.call(entity, direction);
        }
    }

    @WrapOperation(method = "handleDamageEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;handleDamageEvent(Lnet/minecraft/world/damagesource/DamageSource;)V"))
    private void smoothAnim$holdDamageEvent(Entity entity, DamageSource source, Operation<Void> original) {
        if (SmoothAnimationsClient.shouldHoldHurtEffects(entity)) {
            SmoothAnimationsClient.holdHurtEffect(() -> original.call(entity, source));
            return;
        }
        original.call(entity, source);
    }

}
