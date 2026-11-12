package net.mehvahdjukaar.smooth_animations.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @WrapOperation(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/entity/EntityTickList;forEach(Ljava/util/function/Consumer;)V"))
    private void smoothAnim$markEntityTick(EntityTickList list, Consumer<Entity> action,  Operation<Void> original) {
        SinglePlayerTickManager.setInEntityTick(true);
        original.call(list, action);

        SinglePlayerTickManager.setInEntityTick(false);
    }

    @Inject(method = "addFreshEntity", at = @At("RETURN"))
    private void smoothAnim$trackMidTickSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) SinglePlayerTickManager.onServerEntityAdded(entity);
    }

}
