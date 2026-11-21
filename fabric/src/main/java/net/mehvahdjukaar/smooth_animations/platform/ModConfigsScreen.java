package net.mehvahdjukaar.smooth_animations.platform;

import com.mojang.serialization.Codec;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ModConfigsScreen extends OptionsSubScreen {

    private OptionsList list;

    public ModConfigsScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options,
                Component.translatable("smooth_animations.configuration.title").withStyle(ChatFormatting.BOLD));
    }

    @Override
    protected void init() {
        this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        addOptions();
        this.addWidget(this.list);
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.minecraft.setScreen(this.lastScreen))
                .bounds(this.width / 2 - 100, this.height - 27, 200, 20).build());
    }

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
        this.list.addBig(OptionInstance.createBoolean("smooth_animations.configuration." + key, value, setter));
    }

    private <T extends Enum<T>> void addEnum(String key, T value, Consumer<T> setter) {
        Class<T> c = value.getDeclaringClass();
        this.list.addBig(new OptionInstance<>("smooth_animations.configuration." + key, OptionInstance.noTooltip(),
                (t, e) -> Component.literal(e.name().toLowerCase(Locale.ROOT)),
                new OptionInstance.Enum<>(List.of(c.getEnumConstants()), Codec.STRING.xmap(s -> Enum.valueOf(c, s), Enum::name)),
                value, setter));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        this.list.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void removed() {
        super.removed();
        PlatStuffImpl.saveConfigs();
    }
}
