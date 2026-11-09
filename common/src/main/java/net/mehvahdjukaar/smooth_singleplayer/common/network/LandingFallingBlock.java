package net.mehvahdjukaar.smooth_singleplayer.common.network;

//landing is process of being half placed in world and half entity. both exist at sametime for some ticks.
public interface LandingFallingBlock {

    boolean smoothSingleplayer$startLanding();

    boolean smoothSingleplayer$isLanding();

    float smoothSingleplayer$getLandingImpactOffset(float partialTick);
}
