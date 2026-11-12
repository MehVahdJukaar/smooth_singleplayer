package net.mehvahdjukaar.smooth_animations;

import net.mehvahdjukaar.candlelight.api.PlatformImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;

public class PlatStuff {

    @Contract
    @PlatformImpl
    public static <T extends CustomPacketPayload> void registerClientBoundPayload(CustomPacketPayload.Type<T> type,
                                                                                  StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                                                  Consumer<T> handler) {
        throw new AssertionError();
    }
}
