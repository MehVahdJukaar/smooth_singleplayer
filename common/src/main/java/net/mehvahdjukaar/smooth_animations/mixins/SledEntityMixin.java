package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.LerpedOnClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

//snowy spirit
@Pseudo
@Mixin(targets = "net.mehvahdjukaar.snowyspirit.common.entity.SledEntity")
public abstract class SledEntityMixin implements LerpedOnClient {
}
