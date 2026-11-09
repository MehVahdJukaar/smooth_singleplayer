package net.mehvahdjukaar.smooth_animations.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//mega hack. so basically when a client entity falls the block might arrive as its still reaching there due to interpolation and client entites being behind. we cant hide the entity as otherwise animation wont be smooth.
//instead we are forced ot hide the blocks
public class BlockMeshHider {

    //probably dont need to key by level? actually i mgiht ? due to sable and immersive portal. moreover might want a thread local?
    private static final Set<BlockPos> HIDDEN_POS = ConcurrentHashMap.newKeySet();

    public static void hide(BlockPos pos) {
        HIDDEN_POS.add(pos.immutable());
    }

    public static void show(BlockPos pos) {
        if (HIDDEN_POS.remove(pos)) {
            Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public static boolean isHidden(BlockPos pos) {
        return !HIDDEN_POS.isEmpty() && HIDDEN_POS.contains(pos);
    }
}
