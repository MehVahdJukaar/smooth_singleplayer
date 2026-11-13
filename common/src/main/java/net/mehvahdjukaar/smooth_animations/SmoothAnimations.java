package net.mehvahdjukaar.smooth_animations;

import net.mehvahdjukaar.smooth_animations.common.network.ModNetworking;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmoothAnimations {
    public static final String MOD_ID = "smooth_animations";

    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void commonInit() {
        ModNetworking.init();
        PlatStuff.registerConfigs();
    }
}
