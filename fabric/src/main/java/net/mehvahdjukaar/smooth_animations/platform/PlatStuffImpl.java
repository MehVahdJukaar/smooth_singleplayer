package net.mehvahdjukaar.smooth_animations.platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.GsonHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class PlatStuffImpl {

    public static <T extends CustomPacketPayload> void registerClientBoundPayload(CustomPacketPayload.Type<T> type,
                                                                                  StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                                                  Consumer<T> handler) {
        PayloadTypeRegistry.playS2C().register(type, codec);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            PlatStuffClient.registerReceiver(type, handler);
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void registerConfigs() {
        //bare bone config
        Path path = FabricLoader.getInstance().getConfigDir().resolve(SmoothAnimations.MOD_ID + ".json");
        try {
            JsonObject jo = Files.exists(path) ? GsonHelper.parse(Files.readString(path)) : new JsonObject();

            ModConfigs.fallingBlockLanding = GsonHelper.getAsBoolean(jo, "falling_block_landing", true);
            ModConfigs.entityRotationFix = GsonHelper.getAsBoolean(jo, "entity_rotation_fix", true);
            ModConfigs.ghastSwell = GsonHelper.getAsBoolean(jo, "ghast_swell", true);
            ModConfigs.ghastMouthFireball = GsonHelper.getAsBoolean(jo, "ghast_mouth_fireball", true);
            ModConfigs.witherYawFix = GsonHelper.getAsBoolean(jo, "wither_yaw_fix", true);
            ModConfigs.witherModelFix = GsonHelper.getAsBoolean(jo, "wither_model_fix", true);
            ModConfigs.pitchLerpFix = GsonHelper.getAsBoolean(jo, "mob_pitch_lerp", true);
            ModConfigs.showServerHitboxes = GsonHelper.getAsBoolean(jo, "debug_server_hitboxes", false);

            jo.addProperty("entity_rotation_fix", ModConfigs.entityRotationFix);
            jo.addProperty("falling_block_landing", ModConfigs.fallingBlockLanding);
            jo.addProperty("ghast_swell", ModConfigs.ghastSwell);
            jo.addProperty("ghast_mouth_fireball", ModConfigs.ghastMouthFireball);
            jo.addProperty("wither_yaw_fix", ModConfigs.witherYawFix);
            jo.addProperty("wither_model_fix", ModConfigs.witherModelFix);
            jo.addProperty("mob_pitch_lerp", ModConfigs.pitchLerpFix);
            jo.addProperty("debug_server_hitboxes", ModConfigs.showServerHitboxes);
            Files.writeString(path, GSON.toJson(jo));

        } catch (Exception ex) {
            SmoothAnimations.LOGGER.error("Failed to load config {}", path, ex);
        }
    }
}
