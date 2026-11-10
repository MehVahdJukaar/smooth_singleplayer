package net.mehvahdjukaar.smooth_animations.common;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SinglePlayerTickManager {

    public static final long WAIT_LIMIT_NANOS = 200000000L;

    private static volatile boolean clientDrivesTicks;
    //cient gives one per tick, server uses it up
    private static final Semaphore serverTickPermits = new Semaphore(0);
    //so this is basically a boolean but can go > 1 when the server lags or stalls
    private static final AtomicInteger serverTicksBeingAwaitedFor = new AtomicInteger();

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
}
