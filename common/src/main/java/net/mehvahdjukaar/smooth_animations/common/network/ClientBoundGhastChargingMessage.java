package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.mehvahdjukaar.smooth_animations.common.SwellingGhast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundGhastChargingMessage(int entityId, boolean charging) implements CustomPacketPayload {

    public static final Type<ClientBoundGhastChargingMessage> TYPE = new Type<>(SmoothAnimations.res("s2c_ghast_charging"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundGhastChargingMessage> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClientBoundGhastChargingMessage::entityId,
            ByteBufCodecs.BOOL, ClientBoundGhastChargingMessage::charging,
            ClientBoundGhastChargingMessage::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(entityId) instanceof SwellingGhast g) {
            g.smoothSP$setChargingFromServer(charging);
        }
    }
}
