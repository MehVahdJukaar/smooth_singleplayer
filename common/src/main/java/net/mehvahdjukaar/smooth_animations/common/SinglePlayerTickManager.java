package net.mehvahdjukaar.smooth_animations.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

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
}
