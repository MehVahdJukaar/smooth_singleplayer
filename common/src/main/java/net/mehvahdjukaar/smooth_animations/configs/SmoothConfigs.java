package net.mehvahdjukaar.smooth_animations.configs;

import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;
import net.mehvahdjukaar.smooth_animations.SmoothAnimations;

public class SmoothConfigs {

    public static final ModConfigHolder SPEC;

    static {
        ConfigBuilder builder = ConfigBuilder.create(SmoothAnimations.res("common"), ConfigType.COMMON);

        SPEC = builder.build();
        SPEC.forceLoad();
    }

    public static void init() {
    }
}
