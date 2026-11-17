package net.mehvahdjukaar.smooth_animations.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "render", at = @At(value = "FIELD", opcode = 180,
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderHitBoxes:Z"))
    private <E extends Entity> void smoothAnim$showServerHitbox(E entity, double x, double y, double z, float rotationYaw,
                                                                float partialTicks, PoseStack poseStack,
                                                                MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!ModConfigs.showServerHitboxes) {
            return;
        }
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

        //pose is at the client entity render pos
        Vec3 offset = se.getPosition(partialTicks).subtract(entity.getPosition(partialTicks));
        LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.lines()),
                se.getBoundingBox().move(offset.x - se.getX(), offset.y - se.getY(), offset.z - se.getZ()),
                0.7f, 0.2f, 1, 1);
    }
}
