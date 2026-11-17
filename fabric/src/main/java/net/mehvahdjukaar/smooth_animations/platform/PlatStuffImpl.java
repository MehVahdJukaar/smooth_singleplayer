package net.mehvahdjukaar.smooth_animations.platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlatStuffImpl {

    private record ClientBoundType<T>(ResourceLocation id, BiConsumer<T, FriendlyByteBuf> encoder) {
    }

    private static final Map<Class<?>, ClientBoundType<?>> CLIENT_BOUND_TYPES = new HashMap<>();

    public static <T> void registerClientBoundMessage(ResourceLocation id, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                                      Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        CLIENT_BOUND_TYPES.put(type, new ClientBoundType<>(id, encoder));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            PlatStuffClient.registerReceiver(id, decoder, handler);
        }
    }

    @SuppressWarnings("unchecked")
    public static Packet<?> toClientBoundPacket(Object message) {
        var type = (ClientBoundType<Object>) CLIENT_BOUND_TYPES.get(message.getClass());
        FriendlyByteBuf buf = PacketByteBufs.create();
        type.encoder().accept(message, buf);
        return ServerPlayNetworking.createS2CPacket(type.id(), buf);
    }


    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void registerConfigs() {
        //bare bone config
        Path path = FabricLoader.getInstance().getConfigDir().resolve(SmoothAnimations.MOD_ID + ".json");
        try {
            JsonObject jo = Files.exists(path) ? GsonHelper.parse(Files.readString(path)) : new JsonObject();

            ModConfigs.fallingBlockLanding = GsonHelper.getAsBoolean(jo, "falling_block_landing", true);
            ModConfigs.entityRotationFix = GsonHelper.getAsBoolean(jo, "entity_rotation_fix", true);
            try {
                ModConfigs.ghastSwell = ModConfigs.GhastSwell.valueOf(GsonHelper.getAsString(jo, "ghast_swell", "vanilla").toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                ModConfigs.ghastSwell = ModConfigs.GhastSwell.VANILLA;
            }
            ModConfigs.ghastMouthFireball = GsonHelper.getAsBoolean(jo, "ghast_mouth_fireball", true);
            ModConfigs.witherYawFix = GsonHelper.getAsBoolean(jo, "wither_yaw_fix", true);
            ModConfigs.witherModelFix = GsonHelper.getAsBoolean(jo, "wither_model_fix", true);
            ModConfigs.pitchLerpFix = GsonHelper.getAsBoolean(jo, "mob_pitch_lerp", true);
            ModConfigs.showServerHitboxes = GsonHelper.getAsBoolean(jo, "debug_server_hitboxes", false);
            ModConfigs.zeroTickEntitySync = GsonHelper.getAsBoolean(jo, "zero_tick_entity_sync", false);

            jo.addProperty("entity_rotation_fix", ModConfigs.entityRotationFix);
            jo.addProperty("falling_block_landing", ModConfigs.fallingBlockLanding);
            jo.addProperty("ghast_swell", ModConfigs.ghastSwell.name().toLowerCase(Locale.ROOT));
            jo.addProperty("ghast_mouth_fireball", ModConfigs.ghastMouthFireball);
            jo.addProperty("wither_yaw_fix", ModConfigs.witherYawFix);
            jo.addProperty("wither_model_fix", ModConfigs.witherModelFix);
            jo.addProperty("mob_pitch_lerp", ModConfigs.pitchLerpFix);
            jo.addProperty("debug_server_hitboxes", ModConfigs.showServerHitboxes);
            jo.addProperty("zero_tick_entity_sync", ModConfigs.zeroTickEntitySync);
            Files.writeString(path, GSON.toJson(jo));

        } catch (Exception ex) {
            SmoothAnimations.LOGGER.error("Failed to load config {}", path, ex);
        }
    }
}
