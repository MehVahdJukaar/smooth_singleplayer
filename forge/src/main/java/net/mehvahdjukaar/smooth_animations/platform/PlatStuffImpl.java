package net.mehvahdjukaar.smooth_animations.platform;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlatStuffImpl {

    //using acceptMissingOr so vanilla clients can still join over lan
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(SmoothAnimations.res("network"),
            () -> "1", NetworkRegistry.acceptMissingOr("1"), NetworkRegistry.acceptMissingOr("1"));
    private static int messageId = 0;

    public static <T> void registerClientBoundMessage(ResourceLocation id, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder,
                                                      Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        CHANNEL.registerMessage(messageId++, type, encoder, decoder, (message, context) -> {
            context.get().enqueueWork(() -> handler.accept(message));
            context.get().setPacketHandled(true);
        }, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static Packet<?> toClientBoundPacket(Object message) {
        return CHANNEL.toVanillaPacket(message, NetworkDirection.PLAY_TO_CLIENT);
    }


    public static void registerConfigs() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        var entityRotation = builder.comment("Fixes entity bodies rotating choppily when head rotates")
                .define("smooth_rotations", true);
        var fallingBlocks = builder.comment("Smooth transition for falling block entity to block transformation")
                .define("smooth_falling_block_landing", true);
        var ghastSwell = builder.comment("Ghasts will swell up before shooting like they did before 1.3!! :0. VANILLA squashes them like 1.2.5, SWELL also stretches them up so they look like they're taking in air")
                .defineEnum("ghast_swell", ModConfigs.GhastSwell.SWELL);
        var ghastMouthFireball = builder.comment("Ghast fireballs come out of the mouth instead of its eyes")
                .define("ghast_mouth_fireball", true);
        var witherYaw = builder.comment("Fixes withers shaking lika crazy left and right when pathfinding a far target")
                .define("wither_yaw_fix", true);
        var witherModelFix = builder.comment("Fixes many issues with wither model like offcentered head, tail, heads not aiming from the right side or being interpolated")
                .define("wither_model_fix", true);
        var pitchLerp = builder.comment("Fixes sheep and drowned head pitch anim not being interpolated")
                .define("pitch_lerp_fix", true);
        var serverHitboxes = builder.comment("Draws a purple debug box where the integrated server has each entity. Render might jitter a as it's not interpolated")
                .define("debug_server_hitboxes", false);
        var tickSync = builder.comment("Sends entity positions AFTER they move instead ov before like vanilla does. This makes the server and client agree on their position EXACTLY (instead of being 1 tick behind). Projectiles and similar are unaffected and still client driven. Default off as it could be a bit more invasive, turn on for perfect sync")
                .define("zero_tick_entity_sync", false);
        ForgeConfigSpec spec = builder.build();
//TODO: addmore
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec);

        Runnable refresh = () -> {
            ModConfigs.entityRotationFix = entityRotation.get();
            ModConfigs.fallingBlockLanding = fallingBlocks.get();
            ModConfigs.ghastSwell = ghastSwell.get();
            ModConfigs.ghastMouthFireball = ghastMouthFireball.get();
            ModConfigs.witherYawFix = witherYaw.get();
            ModConfigs.witherModelFix = witherModelFix.get();
            ModConfigs.pitchLerpFix = pitchLerp.get();
            ModConfigs.showServerHitboxes = serverHitboxes.get();
            ModConfigs.zeroTickEntitySync = tickSync.get();
        };

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener((ModConfigEvent.Loading e) -> {
            if (e.getConfig().getSpec() == spec) {
                refresh.run();
            }
        });
        bus.addListener((ModConfigEvent.Reloading e) -> {
            if (e.getConfig().getSpec() == spec) {
                refresh.run();
            }
        });
    }
}
