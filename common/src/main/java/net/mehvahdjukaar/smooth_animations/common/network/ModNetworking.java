package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.moonlight.api.platform.network.NetworkHelper;

public class ModNetworking {

    public static void init() {
        //TODO: yeet moonlight deps wo we can run withut other mods
        NetworkHelper.addNetworkRegistration(event -> {
            event.registerClientBoundOptional(ClientBoundNotifyServerTickDoneMessage.CODEC);
        }, 1);
    }
}
