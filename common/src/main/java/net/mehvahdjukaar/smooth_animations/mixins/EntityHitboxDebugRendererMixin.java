package net.mehvahdjukaar.smooth_animations.mixins;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public abstract class EntityHitboxDebugRendererMixin {

    @Inject(method = "showHitboxes", at = @At("TAIL"))
    private void smoothAnim$showServerHitbox(Entity entity, float partialTicks, boolean isServerEntity, CallbackInfo ci) {
        if (!ModConfigs.showServerHitboxes || isServerEntity) return;
        var server = Minecraft.getInstance().getSingleplayerServer();
        if (server == null) {
            return;
        }
        ServerLevel sl = server.getLevel(entity.level().dimension());

        if (sl == null) {
            return;
        }
        Entity se = sl.getEntity(entity.getId());
        if (se == null) {
            return;
        }
        Gizmos.cuboid(se.getBoundingBox().move(se.getPosition(partialTicks).subtract(se.position())),
                GizmoStyle.stroke(ARGB.colorFromFloat(1, 0.7f, 0.2f, 1)));
    }
}
