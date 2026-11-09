package net.mehvahdjukaar.smooth_singleplayer.configs;

import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;
import net.mehvahdjukaar.smooth_singleplayer.SmoothSingleplayer;

public class SmoothConfigs {

    public static final ModConfigHolder SPEC;

    static {
        ConfigBuilder builder = ConfigBuilder.create(SmoothSingleplayer.res("common"), ConfigType.COMMON);

        SPEC = builder.build();
        SPEC.forceLoad();
    }

    public static void init() {
    }
}
