package net.mehvahdjukaar.smooth_animations.common;

//entity interface
public interface FirstServerUpdateAware {

    void smoothAnim$markServerUpdated();
    boolean smoothAnim$shouldWaitForServer();
}
