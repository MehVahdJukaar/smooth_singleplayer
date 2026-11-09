package net.mehvahdjukaar.smooth_singleplayer.mixins;

import net.mehvahdjukaar.smooth_singleplayer.client.BlockMeshHider;
import net.mehvahdjukaar.smooth_singleplayer.common.LandingData;
import net.mehvahdjukaar.smooth_singleplayer.common.network.LandingFallingBlock;
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
    private BlockPos ss$landingPos;
    @Unique
    private float ss$fallSpeedOnLand;
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
        if (ss$landingPos != null) return false;

        BlockPos.MutableBlockPos pos = this.blockPosition().mutable();
        //probes 4 block down till we find our placed block. hopefully enough. idk how correct this is
        for (int i = 0; i < 4; i++) {
            if (level().getBlockState(pos).is(blockState.getBlock())) {
                ss$landingPos = pos.immutable();
                //TODO: this is just wrong. use gravity instead. works for this but might not for others
                ss$fallSpeedOnLand = (float) (Math.abs(getDeltaMovement().y) + 0.04) * 0.98f;
                BlockMeshHider.hide(ss$landingPos);
                return true;
            }
            pos.move(0, -1, 0);
        }
        return false;
    }

    @Override
    public boolean smoothSingleplayer$isLanding() {
        return ss$landingPos != null;
    }

    //fixes the very last bit of fall accounting for the velocity the entity had right before hitting
    @Override
    public float smoothSingleplayer$getLandingImpactOffset(float partialTick) {
        float actualSpeed = ss$fallSpeedOnLand;
        float movedThisTick = (float) (yo - getY());
        if (movedThisTick >= actualSpeed) return 0;
        //if i moved less it means i was blocked.
        float howFarVanillaMovedMe = movedThisTick * partialTick;
        float howFarIShouldHaveMoved = actualSpeed * partialTick;
        return howFarVanillaMovedMe - Math.min(howFarIShouldHaveMoved, movedThisTick);
    }

    //this makes the entity linger and exist for longer, cohexist with the block
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void ss$playLastFallTick(CallbackInfo ci) {
        if (ss$landingPos == null) {
            return;
        }

        ci.cancel();
        if (!ss$hasLanded) {
            this.setPos(Vec3.atBottomCenterOf(ss$landingPos));
            ss$hasLanded = true;
        } else if (ss$landingData == null) {
            ss$landingData = LandingData.capture(ss$landingPos);
            BlockMeshHider.show(ss$landingPos);
        } else {
            //waits max 10 ticks. todo: make configurable
            if (ss$landingData.isNoLongerWaitingMesh() || ++ss$ticksWaitingForMesh > 10) {
                //actually remove
                ((ClientLevel) level()).removeEntity(getId(), RemovalReason.DISCARDED);
            }
        }
    }

    //TODO: yeet out of here! no overrides
    @Override
    public void onClientRemoval() {
        super.onClientRemoval();
        if (ss$landingPos != null) {
            BlockMeshHider.show(ss$landingPos);
        }
    }
}
