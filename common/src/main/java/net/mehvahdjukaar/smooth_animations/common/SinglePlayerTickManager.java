package net.mehvahdjukaar.smooth_animations.common;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SinglePlayerTickManager {

    public static final long WAIT_LIMIT_NANOS = 200000000L;

    private static volatile boolean clientDrivesTicks;
    //cient gives one per tick, server uses it up
    private static final Semaphore serverTickPermits = new Semaphore(0);
    //so this is basically a boolean but can go > 1 when the server lags or stalls
    private static final AtomicInteger serverTicksBeingAwaitedFor = new AtomicInteger();


    //shared map. server sets it and client reads it
    private static final Set<Integer> spawnedMidEntityTick = ConcurrentHashMap.newKeySet();
    //so we might want to make this thread local.. idk
    private static boolean inEntityTick;

    public static boolean isTrueSinglePlayer(@Nullable MinecraftServer server) {
        //published means opened to LAN
        return server != null && !server.isPublished();
    }

    public static boolean isClientDriven() {
        return clientDrivesTicks;
    }
    public static void activateClientTickDrive() {
        serverTickPermits.drainPermits();
        serverTicksBeingAwaitedFor.set(0);
        allowNextServerTick();

        clientDrivesTicks = true;
    }

    public static void disableClientTickDrive() {
        clientDrivesTicks = false;
    }

    public static void allowNextServerTick() {
        serverTicksBeingAwaitedFor.incrementAndGet();

        serverTickPermits.release();
    }

    //server being done means we have received its packet too which happens slightly after end of server tick
    public static boolean hasReceivedAllowedTicks() {
        return serverTicksBeingAwaitedFor.get() <= 0;
    }

    public static void onServerTickDoneReceived() {
        serverTicksBeingAwaitedFor.decrementAndGet();
    }

    public static boolean tryStartServerTick() {
        return !clientDrivesTicks || serverTickPermits.tryAcquire();
    }

    //entity stuff

    public static void setInEntityTick(boolean ticking) {
        inEntityTick = ticking;
    }

    public static void onServerEntityAdded(Entity entity) {
        if (!inEntityTick || !clientDrivesTicks) return;

        //so there are 2 kinds of entities. one mostly server driven and one mostly client driven
        //projectiles are ballistic, client drives most of their trajectory as thats known with sporading server adjustements
        //mobs move by interpolating over server sent positions.
        //mobs thus dont need the 1 time tick freeze, it would be a downgrade
        //inEntityTick detects that stage when entityes added wil be like skelly example
        if (entity instanceof LivingEntity) return;
        spawnedMidEntityTick.add(entity.getId());
    }

    public static boolean consumeHeldEntityTick(Entity entity) {
        return spawnedMidEntityTick.remove(entity.getId());
    }

    public static void clearSpawnedMidEntityTick() {
        spawnedMidEntityTick.clear();
    }

    //sketchy 0 ticking logic

    //vanilla sends entity changes before entities tick so whatever client gets is from last tick
    private static final List<ServerEntity> deferredSendChanges = new ArrayList<>();
    private static boolean sendingDeferredChanges;

    public static boolean shouldDeferSetChanges(ServerEntity serverEntity, Entity entity) {
        if (sendingDeferredChanges || !clientDrivesTicks || !ModConfigs.zeroTickEntitySync) {
            return false;
        }
        //todo: add helper for comomon usage
        boolean lerped = entity instanceof LivingEntity || entity instanceof AbstractMinecart || entity instanceof Boat;
        if (!lerped) {
            return false;
        }
        deferredSendChanges.add(serverEntity);
        return true;
    }

    public static boolean isSendingDeferredChanges() {
        return sendingDeferredChanges;
    }

    public static void sendDeferredSyncs() {
        if (deferredSendChanges.isEmpty()) {
            return;
        }
        sendingDeferredChanges = true;
        for (ServerEntity se : deferredSendChanges) {
            se.sendChanges();
        }
        sendingDeferredChanges = false;

        deferredSendChanges.clear();
    }
}
