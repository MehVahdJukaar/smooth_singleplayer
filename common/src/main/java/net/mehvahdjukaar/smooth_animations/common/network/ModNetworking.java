package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.smooth_animations.PlatStuff;

public class ModNetworking {

    public static void init() {
        PlatStuff.registerClientBoundPayload(ClientBoundNotifyServerTickDoneMessage.TYPE,
                ClientBoundNotifyServerTickDoneMessage.CODEC, ClientBoundNotifyServerTickDoneMessage::handle);
        PlatStuff.registerClientBoundPayload(ClientBoundGhastChargingMessage.TYPE,
                ClientBoundGhastChargingMessage.CODEC, ClientBoundGhastChargingMessage::handle);
    }
}
