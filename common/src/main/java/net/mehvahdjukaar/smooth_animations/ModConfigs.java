package net.mehvahdjukaar.smooth_animations;

public class ModConfigs {

    public static boolean entityRotationFix = true;
    public static boolean fallingBlockLanding = true;
    public static GhastSwell ghastSwell = GhastSwell.VANILLA;
    public static boolean ghastMouthFireball = true;
    public static boolean witherYawFix = true;
    public static boolean witherModelFix = true;
    public static boolean pitchLerpFix = true;
    public static boolean idleYawFollowsBody = true;
    public static boolean snapEntityPosition = true;
    public static boolean showServerHitboxes = false;
    public static boolean zeroTickEntitySync = true;
    public static boolean serverLagFallback = true;
    public static boolean frameTimeCatchUp = true;

    public enum GhastSwell {
        OFF, VANILLA, SWELL
    }
}
