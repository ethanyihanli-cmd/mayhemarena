package com.macondo.mayhemarena;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.macondo.mayhemarena.entity.Bullet;
import com.macondo.mayhemarena.entity.Player;
import com.macondo.mayhemarena.map.MapLoader;
import com.macondo.mayhemarena.ui.HUD;
import com.macondo.mayhemarena.ui.MapSelection;
import com.macondo.mayhemarena.ui.WeaponSelection;
import com.macondo.mayhemarena.weapon.Weapon;
import com.macondo.mayhemarena.weapon.WeaponType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameApp extends GameApplication{

    private Player player1;
    private Player player2;
    private MapLoader mapLoader;
    private Weapon weapon1;
    private Weapon weapon2;
    private List<Bullet> bullets;
    private Set<KeyCode> pressedKeys;
    private HUD hud;

    private WeaponType p1Weapon;
    private WeaponType p2Weapon;
    private String selectedMap;
    private boolean matchStarted;

    public GameApp() {
        pressedKeys = new HashSet<>();
        bullets = new ArrayList<>();
        matchStarted = false;
        selectedMap = "Sky Ruins";
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("MayhemArena");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setIntroEnabled(false);
        settings.setMainMenuEnabled(false);
        settings.setGameMenuEnabled(false);
        settings.setManualResizeEnabled(false);
    }

    @Override
    protected void initGame() {
        MapSelection mapSelection = new MapSelection();
        String map = mapSelection.showAndWait();
        if (map != null) {
            selectedMap = map;
        }

        WeaponSelection selection = new WeaponSelection();
        WeaponType[] selected = selection.showAndWait();

        if (selected == null) {
            p1Weapon = WeaponType.PISTOL;
            p2Weapon = WeaponType.PISTOL;
        } else {
            p1Weapon = selected[0];
            p2Weapon = selected[1];
        }

        mapLoader = new MapLoader();
        mapLoader.loadMap(selectedMap);

        player1 = new Player(1, 500, 360);
        player2 = new Player(2, 780, 360);

        weapon1 = new Weapon(p1Weapon);
        weapon2 = new Weapon(p2Weapon);

        hud = new HUD();

        matchStarted = true;
        setupInput();

        System.out.println("Map: " + selectedMap);
        System.out.println("Player 1: " + p1Weapon.getName());
        System.out.println("Player 2: " + p2Weapon.getName());
    }

    private void setupInput() {
        Scene scene = FXGL.getPrimaryStage().getScene();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            pressedKeys.add(e.getCode());

            if (e.getCode() == KeyCode.SPACE && matchStarted) {
                shoot(player1, weapon1);
            }
            if (e.getCode() == KeyCode.M && matchStarted) {
                shoot(player2, weapon2);
            }
            if (e.getCode() == KeyCode.R && matchStarted) {
                weapon1.reload();
            }
            if (e.getCode() == KeyCode.COMMA && matchStarted) {
                weapon2.reload();
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
             pressedKeys.remove(e.getCode());

             if (e.getCode() == KeyCode.W) {
                  player1.releaseJump();
             }

             if (e.getCode() == KeyCode.UP) {
                 player2.releaseJump();
             }
        });
    }

    private void shoot(Player player, Weapon weapon) {
        if (!weapon.canShoot()) {
            return;
        }

        double x = player.getX();
        double y = player.getY();
        int facing = player.getFacing();

        List<Bullet> newBullets = weapon.shoot(x, y, facing, player.getPlayerId());
        bullets.addAll(newBullets);
    }

    @Override
    protected void onUpdate(double delta) {
        if (!matchStarted) {
            return;
        }

         handleInput(delta);

         player1.update(delta, mapLoader.getPlatforms());
         player2.update(delta, mapLoader.getPlatforms());

         weapon1.update(delta);
         weapon2.update(delta);

         List<Bullet> toRemove = new ArrayList<>();
         for (Bullet b : bullets) {
             b.update(delta);

             if (!b.isActive()) {
                 toRemove.add(b);
                 continue;
             }

             if (b.hitsPlayer(player1)) {
                 player1.takeDamage(b.getDamage());
                 player1.applyKnockback(b.getKnockback(), b.getX() > player1.getX() ? 1 : -1);
                 b.deactivate();
                 toRemove.add(b);
                 continue;
             }

             if (b.hitsPlayer(player2)) {
                 player2.takeDamage(b.getDamage());
                 player2.applyKnockback(b.getKnockback(), b.getX() > player2.getX() ? 1 : -1);
                 b.deactivate();
                 toRemove.add(b);
                 continue;
             }
         }

         bullets.removeAll(toRemove);

         hud.update(player1, player2, weapon1, weapon2);

         checkMatchEnd();
         updateTitle();
    }

    private void checkMatchEnd() {
        if (player1.isKnockedOut()) {
            System.out.println("PLAYER 2 WINS!");
            matchStarted = false;
        } else if (player2.isKnockedOut()) {
            System.out.println("PLAYER 1 WINS!");
            matchStarted = false;
        }
    }

     private void handleInput(double delta) {
         boolean p1left = pressedKeys.contains(KeyCode.A);
         boolean p1right = pressedKeys.contains(KeyCode.D);

         if (p1left && !p1right) {
             player1.moveLeft(delta);
         } else if (p1right && !p1left) {
             player1.moveRight(delta);
         } else {
             player1.stopMoving();
         }

         if (pressedKeys.contains(KeyCode.W) {
             player1.jump();
         }

         boolean p2Left = pressedKeys.contains(KeyCode.LEFT);
         boolean p2Right = pressedKeys.contains(KeyCode.RIGHT);

         if (p2Left && !p2Right) {
             player2.moveLeft(delta);
         } else if (p2Right && !p2Left) {
             player2.moveRight(delta);
         } else {
             player2.stopMoving();
         }

         if (pressedKeys.contains(KeyCode.UP)) {
             player2.jump();
         }

     }

     private void updateTitle() {
        String p1Health = "HP: " + player1.getHealth() + "/" + player1.getMaxHealth();
        String p2Health = "HP: " + player2.getHealth() + "/" + player2.getMaxHealth();

        String p1Ammo = weapon1.getMagazine() + "/" + weapon1.getMaxMagazine() +
                " | " + weapon1.getCurrentAmmo() + "/" + weapon1.getMaxAmmo();
        String p2Ammo = weapon2.getMagazine() + "/" + weapon2.getMaxMagazine() +
                " | " + weapon2.getCurrentAmmo() + "/" + weapon2.getMaxAmmo();

        FXGL.getSettings().setTitle(selectedMap + " | P1: " + p1Health + " " + p1Ammo +
                " | P2: " + p2Health + " " + p2Ammo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

