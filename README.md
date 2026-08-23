# MayhemArena

A physics arena fighting game where the goal is simple: do not get knocked off and do not get knocked out. Players jump around floating platforms, shoot different weapons, use perks, and try to win rounds by surviving longer than the opponent.

## Why I made it

When I was younger, I was obsessed with those unblocked games people played on school Wi-Fi. I would literally search up "most fun unblocked games" and spend my study hall trying random arena games, fighting games, and simple browser games. A couple days ago I had a conversation about this with an old friend, and it brought back a vivid memory of those games. I wanted to see if I could recreate that same kind of chaotic, quick, fun game with the help of my friend.

The idea became MayhemArena: an arena that follows physics, where players can jump, fall, shoot, get knocked around, and lose by taking too much damage or falling off the map.

This is my best project and also my most disappointing project at the same time. At first, when I created the game, I did not even like it myself. That made me think that if I did not like playing it, other people probably would not like it either. So I changed it majorly. I went through a bunch of challenges, changed a lot of my original ideas, rebuilt parts of the gameplay, and turned it into something better. Now it is the best game I have created so far, and I am very happy with it.

## Tools

Main version: Java 21 + JavaFX + FXGL. Uses Maven.

There is also an HTML version in `index.html` with plain HTML, CSS, and JavaScript. It does not need any install.

## Features

- Physics-based movement with gravity, jumping, falling, and knockback
- Multiple maps with platforms and pits
- Player vs bot or local player vs player
- Weapons like pistol, rifle, shotgun, sniper, and knife
- Perks like double jump, speed boost, knockback resist, and health boost
- Rounds, win tracking, health, ammo, reloads, and sound effects
- Custom controls in the Java version

## Run the Java version

Make sure Java 21 is installed. Then run this in the project folder:

- Mac/Linux: `./mvnw javafx:run`
- Windows: `mvnw.cmd javafx:run`

## Run the HTML version

Open `index.html` in any browser.

Default controls:

- Player 1: `A` and `D` to move, `W` to jump, `S` to drop faster, `Space` to shoot, `R` to reload
- Player 2: arrow keys to move and jump, `Down` to drop faster, `M` to shoot, `,` to reload

## How to rebuild it yourself

1. Create a game window or canvas.
2. Add players with position, velocity, health, facing direction, and grounded state.
3. Add gravity so players fall every frame.
4. Add platforms and check if players land on top of them.
5. Add movement, jumping, falling through platforms, and screen boundaries.
6. Add weapons with damage, ammo, reload time, cooldown, bullet speed, recoil, and knockback.
7. Create bullets that move every frame and disappear after a short time.
8. Check bullet collisions against players.
9. Apply damage and knockback when a bullet hits.
10. End the round when a player is knocked out or falls below the arena.
11. Track round wins and end the match when one player wins enough rounds.
12. Add maps, perks, menus, UI, sounds, and polish after the core game works.

In the Java version, I split the game into classes for players, bots, bullets, weapons, maps, menus, HUD, and match control. The HTML version keeps everything in one file so it is easier to open and understand.
