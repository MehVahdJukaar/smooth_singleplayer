package net.mehvahdjukaar.smooth_animations.client;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.common.SinglePlayerTickManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class SmoothAnimationsClient {

    private static final List<Runnable> heldHurtEffects = new ArrayList<>();


    public static boolean isTickLockedToServer() {
        return SinglePlayerTickManager.isTrueSinglePlayer(Minecraft.getInstance().getSingleplayerServer())
                && !SinglePlayerTickManager.isServerLagging();
    }

    public static boolean shouldHoldHurtEffects(Entity entity) {
        return (ModConfigs.zeroTickEntitySync && SinglePlayerTickManager.isClientDriven()) && isTickLockedToServer()
                && SinglePlayerTickManager.isLerpedOnClient(entity) && !(entity instanceof LocalPlayer);
    }

    public static void holdHurtEffect(Runnable effect) {
        heldHurtEffects.add(effect);
    }

    public static void releaseHeldHurtEffects(){
        for (Runnable effect : heldHurtEffects) {
            effect.run();
        }
        heldHurtEffects.clear();
    }
}
