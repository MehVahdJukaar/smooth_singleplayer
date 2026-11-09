package net.mehvahdjukaar.smooth_singleplayer.platform;

import net.mehvahdjukaar.smooth_singleplayer.SmoothSingleplayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SmoothSingleplayer.MOD_ID)
public class SmoothSingleplayerForge {

    public SmoothSingleplayerForge(IEventBus busEvent) {
        SmoothSingleplayer.commonInit();
    }
}
