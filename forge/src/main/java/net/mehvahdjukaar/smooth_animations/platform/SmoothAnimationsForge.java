package net.mehvahdjukaar.smooth_animations.platform;

import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SmoothAnimations.MOD_ID)
public class SmoothAnimationsForge {

    public SmoothAnimationsForge(IEventBus busEvent) {
        SmoothAnimations.commonInit();
    }
}
