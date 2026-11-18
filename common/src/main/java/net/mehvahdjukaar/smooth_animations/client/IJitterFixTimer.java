package net.mehvahdjukaar.smooth_animations.client;

public interface IJitterFixTimer {

    void smoothAnim$freezeTick();

    void smoothAnim$catchUpPartialTick(long time);
}
