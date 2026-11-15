package net.mehvahdjukaar.smooth_animations.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;

//own class so dedicated servers never load ClientPlayNetworking
public class PlatStuffClient {

    public static <T> void registerReceiver(ResourceLocation id, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, listener, buf, sender) -> {
            T m = decoder.apply(buf);
            client.execute(() -> handler.accept(m));
        });
    }
}
