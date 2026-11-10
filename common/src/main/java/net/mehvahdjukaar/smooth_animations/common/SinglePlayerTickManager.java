package net.mehvahdjukaar.smooth_animations.common;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public class SinglePlayerTickManager {

    public static boolean isActive(@Nullable MinecraftServer server) {
        //published means dedicated
        return server != null && !server.isPublished();
    }
}
