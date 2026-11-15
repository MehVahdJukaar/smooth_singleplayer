package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.mehvahdjukaar.smooth_animations.common.SwellingGhast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundGhastChargingMessage(int entityId, boolean charging) {

    public static final ResourceLocation ID = SmoothAnimations.res("s2c_ghast_charging");

    public ClientBoundGhastChargingMessage(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeBoolean(charging);
    }

    public void handle() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(entityId) instanceof SwellingGhast g) {
            g.smoothSP$setChargingFromServer(charging);
        }
    }
}
