package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundNotifyServerTickDoneMessage() implements CustomPacketPayload {

    public static final ClientBoundNotifyServerTickDoneMessage INSTANCE = new ClientBoundNotifyServerTickDoneMessage();


    public static final Type<ClientBoundNotifyServerTickDoneMessage> TYPE = new Type<>(SmoothAnimations.res("s2c_server_tick_done"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundNotifyServerTickDoneMessage> CODEC = StreamCodec.unit(INSTANCE);


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public void handle() {
        SinglePlayerTickManager.onServerTickDoneReceived();
    }
}
