package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.client.ChargingGhastState;
import net.minecraft.client.renderer.entity.state.GhastRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GhastRenderState.class)
public abstract class GhastRenderStateMixin implements ChargingGhastState {

    @Unique
    private float ss$charge;

    @Override
    public void smoothSP$setCharge(float charge){
        ss$charge = charge;
    }


    @Override
    public float smoothSP$getCharge(){
        return ss$charge;
    }
}
