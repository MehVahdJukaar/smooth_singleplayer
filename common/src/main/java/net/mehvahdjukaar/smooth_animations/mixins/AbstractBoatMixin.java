package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.LerpedOnClient;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin implements LerpedOnClient {
}
