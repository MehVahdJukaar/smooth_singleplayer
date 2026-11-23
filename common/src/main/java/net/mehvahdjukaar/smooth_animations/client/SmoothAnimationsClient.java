package net.mehvahdjukaar.smooth_animations.client;

import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.Minecraft;

public class SmoothAnimationsClient {

    public static boolean isTickLockedToServer() {
        return SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer())
                && !SinglePlayerTickManager.isServerLagging();
    }
}
