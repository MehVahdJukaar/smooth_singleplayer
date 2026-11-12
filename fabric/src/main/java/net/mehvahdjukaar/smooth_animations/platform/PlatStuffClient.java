package net.mehvahdjukaar.smooth_animations.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;

//own class so dedicated servers never load ClientPlayNetworking
public class PlatStuffClient {

    public static <T extends CustomPacketPayload> void registerReceiver(CustomPacketPayload.Type<T> type, Consumer<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> handler.accept(payload));
    }
}
