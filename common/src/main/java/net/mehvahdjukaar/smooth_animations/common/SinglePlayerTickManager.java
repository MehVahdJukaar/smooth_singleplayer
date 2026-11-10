package net.mehvahdjukaar.smooth_animations.common;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicLong;

public class SinglePlayerTickManager {

    private static volatile boolean clientDrivesTicks;
    //these 2 only ever go up. obviously no thread local as these are accessed by 2 threads
    private static final AtomicLong serverTicksAllowed = new AtomicLong();
    private static final AtomicLong serverTicksCompleted = new AtomicLong();

    public static boolean isTrueSinglePlayer(@Nullable MinecraftServer server) {
        //published means opened to LAN
        return server != null && !server.isPublished();
    }

    public static boolean isClientDriven() {
        return clientDrivesTicks;
    }
    public static void activateClientTickDrive() {
        //reset to match
        serverTicksAllowed.set(serverTicksCompleted.get());
        allowNextServerTick();

        clientDrivesTicks = true;
    }

    public static void disableClientTickDrive() {
        clientDrivesTicks = false;
    }

    public static void allowNextServerTick() {
        serverTicksAllowed.incrementAndGet();
    }

    public static boolean isServerTickDone() {
        return serverTicksCompleted.get() >= serverTicksAllowed.get();
    }

    public static boolean mayServerTick() {
        return !clientDrivesTicks || serverTicksCompleted.get() < serverTicksAllowed.get();
    }

    public static void onServerTickDone() {
        serverTicksCompleted.incrementAndGet();
    }
}
