package net.mehvahdjukaar.smooth_animations.platform;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.mehvahdjukaar.smooth_animations.SmoothAnimations;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.function.Consumer;

public class PlatStuffImpl {

    public static <T extends CustomPacketPayload> void registerClientBoundPayload(CustomPacketPayload.Type<T> type,
                                                                                  StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                                                  Consumer<T> handler) {
        IEventBus bus = ModList.get().getModContainerById(SmoothAnimations.MOD_ID).orElseThrow().getEventBus();
        bus.addListener(RegisterPayloadHandlersEvent.class, event -> event.registrar("1").optional()
                .playToClient(type, codec, (payload, context) -> handler.accept(payload)));
    }


    public static void registerConfigs() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        var entityRotation = builder.comment("Fixes entity bodies rotating choppily when head rotates")
                .define("smooth_body_turning", true);
        var snapPosition = builder.comment("Entities snap to their server position every tick. Off makes them trail behind it over 3 ticks like vanilla. Mobs still turn over a few ticks either way, snapping that too makes them flick around. True singleplayer.")
                .define("snap_entity_position", true);
        var pitchLerp = builder.comment("Fixes sheep and drowned head pitch anim not being interpolated")
                .define("pitch_lerp_fix", true);
        var slimeSquish = builder.comment("Fixes slime and magma cube squish not being interpolated")
                .define("slime_squish_lerp", true);
        var fallingBlocks = builder.comment("Smooth transition for falling block entity to block transformation")
                .define("smooth_falling_block_landing", true);
        var witherYaw = builder.comment("Fixes withers shaking lika crazy left and right when pathfinding a far target")
                .define("wither_yaw_fix", true);
        var witherModelFix = builder.comment("Fixes many issues with wither model like offcentered head, tail, heads not aiming from the right side or being interpolated")
                .define("wither_model_fix", true);
        var ghastMouthFireball = builder.comment("Ghast fireballs come out of the mouth instead of its eyes")
                .define("ghast_mouth_fireball", true);
        var ghastSwell = builder.comment("Ghasts will swell up before shooting like they did before 1.3!! :0. VANILLA squashes them like 1.2.5, SWELL also stretches them up so they look like they're taking in air")
                .defineEnum("ghast_swell", ModConfigs.GhastSwell.VANILLA);
        var tickSync = builder.comment("Sends entity positions AFTER they move instead ov before like vanilla does. This makes the server and client agree on their position EXACTLY (instead of being 1 tick behind). Projectiles and similar are unaffected and still client driven. Default off as it could be a bit more invasive, turn on for perfect sync")
                .define("zero_tick_entity_sync", false);
        var serverLagFallback = builder.comment("When the server lags, the client stops waiting for it and runs on its own like vanilla, until the server catches up. Off makes the whole game slow down with the server, player included. No more 'client' or 'server' lag, just lag. True singleplayer.")
                .define("server_lag_fallback", true);
        var serverHitboxes = builder.comment("Draws a purple debug box where the integrated server has each entity. Render might jitter a as it's not interpolated")
                .define("debug_server_hitboxes", false);
        ModConfigSpec spec = builder.build();
        ModContainer c = ModList.get().getModContainerById(SmoothAnimations.MOD_ID).orElseThrow();
//TODO: addmore
        c.registerConfig(ModConfig.Type.COMMON, spec);


        if (FMLEnvironment.dist.isClient()) {
            c.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        Runnable refresh = () -> {
            ModConfigs.entityRotationFix = entityRotation.get();
            ModConfigs.fallingBlockLanding = fallingBlocks.get();
            ModConfigs.ghastSwell = ghastSwell.get();
            ModConfigs.ghastMouthFireball = ghastMouthFireball.get();
            ModConfigs.witherYawFix = witherYaw.get();
            ModConfigs.witherModelFix = witherModelFix.get();
            ModConfigs.pitchLerpFix = pitchLerp.get();
            ModConfigs.slimeSquishLerp = slimeSquish.get();
            ModConfigs.snapEntityPosition = snapPosition.get();
            ModConfigs.showServerHitboxes = serverHitboxes.get();
            ModConfigs.zeroTickEntitySync = tickSync.get();
            ModConfigs.serverLagFallback = serverLagFallback.get();
        };

        IEventBus bus = c.getEventBus();
        bus.addListener(ModConfigEvent.Loading.class, e -> {
            if (e.getConfig().getSpec() == spec) {
                refresh.run();
            }
        });
        bus.addListener(ModConfigEvent.Reloading.class, e -> {
            if (e.getConfig().getSpec() == spec) {
                refresh.run();
            }
        });
    }
}
