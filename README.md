# Singleplayer Jitter Fix: Smooth Entity Animations

<p align="center">
  <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/sand.gif" width="24%" alt="Falling blocks">
  <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/piglin.gif" width="24%" alt="Mob turning">
  <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/chicken.gif" width="24%" alt="Mob movement">
  <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/snowball.gif" width="24%" alt="Projectiles">
</p>

Ever since beta 1.3 the game has been PLAGUED by this very controlversion change: The client- serveer split.

If you dont know even on singleplayer your client runs a internal SERVER!

This causes a slew of desyncs and jittery behavior that is completley avoidable in single player.

This mod is simple in scope and simply addresses this issue with a series of targeted chanfes that fix all known smoothness issues.

## Changes:

*   Server and Client now tick in alternating mode. one tick one, one the other
*   All entities "send" their position every tick. this means they are always 1 tick behind as opposed to several in vanilla
*   Fixes entities spawned in wrong phase staritng on client with a wrong position causing entity snapback when server updates them
*   Fixes falling blocks disappearing as soon as the block is placed. Now they disappear when the chunk mesh has hpdated instead, perfectluy smooth.

![meme](https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/tick_sync_meme.png)
