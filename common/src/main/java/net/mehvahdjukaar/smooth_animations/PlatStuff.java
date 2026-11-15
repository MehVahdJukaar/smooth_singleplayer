package net.mehvahdjukaar.smooth_animations;

import net.mehvahdjukaar.candlelight.api.PlatformImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlatStuff {

    @Contract
    @PlatformImpl
    public static <T> void registerClientBoundMessage(ResourceLocation id, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                                      Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        throw new AssertionError();
    }

    @Contract
    @PlatformImpl
    public static Packet<?> toClientBoundPacket(Object message) {
        throw new AssertionError();
    }

    @Contract
    @PlatformImpl
    public static void registerConfigs() {
        throw new AssertionError();
    }
}
