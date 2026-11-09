package net.mehvahdjukaar.smooth_singleplayer.mixins;

import net.mehvahdjukaar.smooth_singleplayer.common.LandingData;
import net.mehvahdjukaar.smooth_singleplayer.common.network.LandingFallingBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityLandingMixin extends Entity implements LandingFallingBlock {

    @Shadow
    private BlockState blockState;

    @Unique
    @Nullable
    private LandingData ss$landingData;

    @Unique
    private boolean ss$hasLanded;
    @Unique
    private int ss$ticksWaitingForMesh;

    protected FallingBlockEntityLandingMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    //TODO: dont use override. not compatible with other mods
    @Override
    public boolean smoothSingleplayer$startLanding() {
        if (ss$landingData != null) return false;

        BlockPos.MutableBlockPos pos = this.blockPosition().mutable();
        //probes 4 block down till we find our placed block. hopefully enough. idk how correct this is
        for (int i = 0; i < 4; i++) {
            if (level().getBlockState(pos).is(blockState.getBlock())) {
                var section = Minecraft.getInstance().levelRenderer.viewArea.getRenderSectionAt(pos);
                var compiledSec = section == null ? null : section.compiled.get();
                ss$landingData = new LandingData(pos.immutable(), section, compiledSec);
                return true;
            }
            pos.move(0, -1, 0);
        }
        return false;
    }

    //this makes the entity linger and exist for longer, cohexist with the block
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void ss$playLastFallTick(CallbackInfo ci) {
        if (ss$landingData == null) return;

        ci.cancel();
        if (ss$hasLanded) {
            //waits max 10 ticks. todo: make configurable
            if (ss$landingData.isNoLongerWaitingMesh() || ++ss$ticksWaitingForMesh > 10) {
                //actually remove
                ((ClientLevel) level()).removeEntity(getId(), RemovalReason.DISCARDED);
            }
        } else {
            this.setPos(Vec3.atBottomCenterOf(ss$landingData.pos()));
            ss$hasLanded = true;
        }
    }
}
