package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundNotifyServerTickDoneMessage() implements Message {

    public static final TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundNotifyServerTickDoneMessage> CODEC =
            Message.makeType(SmoothAnimations.res("s2c_server_tick_done"), ClientBoundNotifyServerTickDoneMessage::new);

    public ClientBoundNotifyServerTickDoneMessage(RegistryFriendlyByteBuf buffer) {
        this();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CODEC.type();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buffer) {
    }

    @Override
    public void handle(Context context) {
        SinglePlayerTickManager.onServerTickDoneReceived();
    }
}
