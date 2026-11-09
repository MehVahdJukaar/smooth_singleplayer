package net.mehvahdjukaar.smooth_singleplayer;

import net.mehvahdjukaar.smooth_singleplayer.configs.SmoothConfigs;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmoothSingleplayer {
    public static final String MOD_ID = "smooth_singleplayer";

    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void commonInit() {
        SmoothConfigs.init();
    }
}
