package net.mehvahdjukaar.smooth_animations.platform;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

public class ModConfigsScreen extends OptionsSubScreen {

    public ModConfigsScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options,
                Component.translatable("smooth_animations.configuration.title").withStyle(ChatFormatting.BOLD));
    }

    @Override
    protected void addOptions() {
        addBool("smooth_body_turning", ModConfigs.entityRotationFix, v -> ModConfigs.entityRotationFix = v);
        addBool("pitch_lerp_fix", ModConfigs.pitchLerpFix, v -> ModConfigs.pitchLerpFix = v);
        addBool("slime_squish_lerp", ModConfigs.slimeSquishLerp, v -> ModConfigs.slimeSquishLerp = v);
        addBool("smooth_falling_block_landing", ModConfigs.fallingBlockLanding, v -> ModConfigs.fallingBlockLanding = v);
        addBool("wither_yaw_fix", ModConfigs.witherYawFix, v -> ModConfigs.witherYawFix = v);
        addBool("wither_model_fix", ModConfigs.witherModelFix, v -> ModConfigs.witherModelFix = v);
        addBool("ghast_mouth_fireball", ModConfigs.ghastMouthFireball, v -> ModConfigs.ghastMouthFireball = v);
        addEnum("ghast_swell", ModConfigs.ghastSwell, v -> ModConfigs.ghastSwell = v);
        addBool("zero_tick_entity_sync", ModConfigs.zeroTickEntitySync, v -> ModConfigs.zeroTickEntitySync = v);
        addBool("server_lag_fallback", ModConfigs.serverLagFallback, v -> ModConfigs.serverLagFallback = v);
        addBool("debug_server_hitboxes", ModConfigs.showServerHitboxes, v -> ModConfigs.showServerHitboxes = v);
    }

    private void addBool(String key, boolean value, Consumer<Boolean> setter) {
        this.list.addSmall(CycleButton.onOffBuilder(value)
                .create(0, 0, 310, 20, Component.translatable("smooth_animations.configuration." + key),
                        (b, v) -> setter.accept(v)), null);
    }

    private <T extends Enum<T>> void addEnum(String key, T value, Consumer<T> setter) {
        this.list.addSmall(CycleButton.<T>builder(e -> Component.literal(e.name().toLowerCase(Locale.ROOT)))
                .withValues(value.getDeclaringClass().getEnumConstants())
                .withInitialValue(value)
                .create(0, 0, 310, 20, Component.translatable("smooth_animations.configuration." + key),
                        (b, v) -> setter.accept(v)), null);
    }

    @Override
    public void removed() {
        super.removed();
        PlatStuffImpl.saveConfigs();
    }
}
