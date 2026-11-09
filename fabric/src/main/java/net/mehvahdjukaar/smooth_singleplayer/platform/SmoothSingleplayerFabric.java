package net.mehvahdjukaar.smooth_singleplayer.platform;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.smooth_singleplayer.SmoothSingleplayer;

public class SmoothSingleplayerFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SmoothSingleplayer.commonInit();
    }
}
