package net.mehvahdjukaar.smooth_animations.common;

import net.mehvahdjukaar.smooth_animations.ModConfigs;
import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SinglePlayerTickManager {

    //mc constants just to make the following clearer
    //this is 50ms
    private static final long NANOS_PER_TICK = TimeUtil.NANOSECONDS_PER_SECOND / SharedConstants.TICKS_PER_SECOND;

    //this is the longest either one blocks the other one. ever.
    //on client (with server lag) with the lag_fallback this is actually not used as other wins.
    public static final long MAX_LOCK_WAIT_NANO = 4 * NANOS_PER_TICK;
    //for lag_fallback. after server is below 18 tps we unlock
    //this basically mean: IF we are waiting for DOUBLE the MAXIMUM time the server is allowd to tick BEFORE it goes down its target of 20 fps, THEN we tick anywyas
    private static final long MAX_SERVER_LAG_FALLBACK_WAIT_NANO = 2 * NANOS_PER_TICK;
    //controls how long a server lag spike is remembered. Needed as server could have very fast and very slow ticks in quick succession
    private static final long WAIT_DEBT_DECAY_NANOS = NANOS_PER_TICK / 10;
    //some slack when we lagging
    private static final int MAX_QUEUED_SERVER_TICKS = 2;

    //if our locking is on or off
    private static volatile boolean clientDrivesTicks;
    //cient gives one per tick, server uses it up
    private static final Semaphore serverTickPermits = new Semaphore(0);
    //so this is basically a boolean but can go > 1 when the server lags or stalls. How many ticks we awaitng for
    private static final AtomicInteger serverTicksBeingAwaitedFor = new AtomicInteger();

    //for server lag detection
    private static long startedWaitingNanos;
    private static long serverWaitDebtNanos;
    //fuck it, server is lagging as shit we unlocked
    private static boolean serverLagging;


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
        serverLagging = false;
        serverWaitDebtNanos = 0;
        allowNextServerTick();

        clientDrivesTicks = true;
    }

    public static void disableClientTickDrive() {
        clientDrivesTicks = false;
    }

    public static void allowNextServerTick() {
        if (startedWaitingNanos != 0) {
            serverWaitDebtNanos += System.nanoTime() - startedWaitingNanos;
            startedWaitingNanos = 0;
        } else {
            serverWaitDebtNanos = Math.max(0, serverWaitDebtNanos - WAIT_DEBT_DECAY_NANOS);
        }

        if (serverTicksBeingAwaitedFor.get() >= MAX_QUEUED_SERVER_TICKS) return;
        serverTicksBeingAwaitedFor.incrementAndGet();

        serverTickPermits.release();
    }

    //server being done means we have received its packet too which happens slightly after end of server tick
    public static boolean hasReceivedAllowedTicks() {
        return serverTicksBeingAwaitedFor.get() <= 0;
    }

    public static boolean shouldWaitForServer() {
        if (hasReceivedAllowedTicks()) {
            serverLagging = false;
            return false;
        }
        if (serverLagging) return false;

        long now = System.nanoTime();
        if (startedWaitingNanos == 0) startedWaitingNanos = now;

        boolean waitedTooMuch = serverWaitDebtNanos + now - startedWaitingNanos > MAX_SERVER_LAG_FALLBACK_WAIT_NANO;
        if (ModConfigs.serverLagFallback && waitedTooMuch) {
            serverLagging = true;
            serverWaitDebtNanos = 0;
            startedWaitingNanos = 0;
            return false;
        }
        return true;
    }


    public static void onServerTickDoneReceived() {
        serverTicksBeingAwaitedFor.updateAndGet(i -> Math.max(0, i - 1));
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
        boolean lerped = entity instanceof LivingEntity || entity instanceof LerpedOnClient;
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
