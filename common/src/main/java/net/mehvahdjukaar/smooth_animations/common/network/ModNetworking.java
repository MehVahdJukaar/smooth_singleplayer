package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.smooth_animations.PlatStuff;

public class ModNetworking {

    public static void init() {
        PlatStuff.registerClientBoundMessage(ClientBoundNotifyServerTickDoneMessage.ID, ClientBoundNotifyServerTickDoneMessage.class,
                ClientBoundNotifyServerTickDoneMessage::write, ClientBoundNotifyServerTickDoneMessage::read, ClientBoundNotifyServerTickDoneMessage::handle);
        PlatStuff.registerClientBoundMessage(ClientBoundGhastChargingMessage.ID, ClientBoundGhastChargingMessage.class,
                ClientBoundGhastChargingMessage::write, ClientBoundGhastChargingMessage::new, ClientBoundGhastChargingMessage::handle);
    }
}
