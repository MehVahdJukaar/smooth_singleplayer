package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.common.FirstServerUpdateAware;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements FirstServerUpdateAware {

    @Unique
    private boolean smoothAnim$serverUpdated;
    @Unique
    private int smoothAnim$ticksWaited;

    @Override
    public void smoothAnim$markServerUpdated() {
        smoothAnim$serverUpdated = true;
    }

    @Override
    public boolean smoothAnim$shouldWaitForServer() {
        if (smoothAnim$serverUpdated || !SinglePlayerTickManager.isClientDriven()) return false;

        //so there are 2 kinds of entities. one mostly server driven and one mostly client driven
        //projectiles are ballistic, client drives most of their trajectory as thats known with sporading server adjustements
        //mobs move by interpolating over server sent positions.
        //mobs thus dont need the 1 time tick freeze, it would be a downgrade
        if ((Object) this instanceof LivingEntity) return false;
        return smoothAnim$ticksWaited++ < 3;
    }
}
