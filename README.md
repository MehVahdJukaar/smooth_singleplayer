# Singleplayer Jitter Fix: Smooth Entity Animations

<p align="center">
  <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/sand.gif" width="23%" alt="Falling blocks"> <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/piglin.gif" width="23%" alt="Mob turning"> <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/chicken.gif" width="23%" alt="Mob movement"> <img src="https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/snowball.gif" width="23%" alt="Projectiles">
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
*   Mob bodies follow the head every tick with an eased catch up instead of snapping every 15 degrees. Heads slow down near their look target
*   Partial tick gets recomputed after the client tick runs so frames that run a tick aren't drawn in the past
*   Brings back & improves the ghast swelling up before shooting, gone since beta 1.3 (MC-165038)
*   Fixes withers shaking left and right when chasing a far target (MC-272049)
*   Fixes many issues with wither model like offcentered head, tail, heads not aiming from the right side or being interpolated

## Multiplayer

| feature | multiplayer |
|---|---|
| client/server tick lockstep | no |
| fresh positions every tick | no |
| projectiles fired from AI | yes, needs mod on server |
| falling block landing | yes |
| mob bodies | yes |
| mob heads | yes, needs mod on server |
| partial tick catch up | yes |
| ghast swell | yes |
| wither yaw fix | yes, needs mod on server |
| wither model fixes | yes |

![meme](https://raw.githubusercontent.com/MehVahdJukaar/mod_pages/master/assets/media/singleplayer-jitter-fix/tick_sync_meme.png)
