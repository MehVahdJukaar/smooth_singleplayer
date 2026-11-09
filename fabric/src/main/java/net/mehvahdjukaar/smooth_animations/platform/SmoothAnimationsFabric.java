package net.mehvahdjukaar.smooth_animations.platform;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.smooth_animations.SmoothAnimations;

public class SmoothAnimationsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SmoothAnimations.commonInit();
    }
}
