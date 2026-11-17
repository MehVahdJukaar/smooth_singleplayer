package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.LerpedOnClient;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin implements LerpedOnClient {
}
