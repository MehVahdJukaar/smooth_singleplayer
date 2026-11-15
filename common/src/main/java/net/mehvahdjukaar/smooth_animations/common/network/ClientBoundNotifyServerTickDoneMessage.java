package net.mehvahdjukaar.smooth_animations.common.network;

import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientBoundNotifyServerTickDoneMessage() {

    public static final ClientBoundNotifyServerTickDoneMessage INSTANCE = new ClientBoundNotifyServerTickDoneMessage();


    public static final ResourceLocation ID = SmoothAnimations.res("s2c_server_tick_done");

    public static ClientBoundNotifyServerTickDoneMessage read(FriendlyByteBuf buf) {
        return INSTANCE;
    }

    public void write(FriendlyByteBuf buf) {
    }


    public void handle() {
        SinglePlayerTickManager.onServerTickDoneReceived();
    }
}
