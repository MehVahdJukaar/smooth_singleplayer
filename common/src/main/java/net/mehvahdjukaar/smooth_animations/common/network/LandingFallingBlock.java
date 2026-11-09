package net.mehvahdjukaar.smooth_animations.common.network;

//landing is process of being half placed in world and half entity. both exist at sametime for some ticks.
public interface LandingFallingBlock {

    boolean smoothSP$startLanding();

    boolean smoothSP$isLanding();

    float smoothSP$getLandingRenderYOffset(float partialTick);
}
