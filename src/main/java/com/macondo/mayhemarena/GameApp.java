package com.macondo.mayhemarena;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.macondo.mayhemarena.entity.Bullet;
import com.macondo.mayhemarena.entity.Player;
import com.macondo.mayhemarena.map.MapLoader;
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

    private Player player;
    private MapLoader mapLoader;
    private Weapon weapon;
    private List<Bullet> bullets;
    private Set<KeyCode> pressedKeys;

    public GameApp() {
        pressedKeys = new HashSet<>();
        bullets = new ArrayList<>();
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
        player = new Player();
        mapLoader = new MapLoader();
        mapLoader.loadMap("Sky Ruins");
        weapon = new Weapon(WeaponType.PISTOL);
        setupInput();
    }

    private void setupInput() {
        Scene scene = FXGL.getPrimaryStage().getScene();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            pressedKeys.add(e.getCode());

            if (e.getCode() == KeyCode.SPACE) {
                shoot();
            }
            if (e.getCode() == KeyCode.R) {
                weapon.reload();
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
             pressedKeys.remove(e.getCode());
             if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
                  player.releaseJump();
             }
        });
    }

    private void shoot() {
        if (!weapon.canShoot()) {
            return;
        }

        double x = player.getX();
        double y = player.getY();
        int facing = player.getFacing();

        List<Bullet> newBullets = weapon.shoot(x, y, facing);
        bullets.addAll(newBullets);
    }

    @Override
    protected void onUpdate(double delta) {
         handleInput(delta);

         player.update(delta, mapLoader.getPlatforms());

         weapon.update(delta);

         List<Bullet> toRemove = new ArrayList<>();
         for (Bullet b : bullets) {
             b.update(delta);
             if (!b.isActive()) {
                 toRemove.add(b);
             }
         }

         bullets.removeAll(toRemove);

         updateTitle();
    }

     private void handleInput(double delta) {
         boolean left = pressedKeys.contains(KeyCode.A) || pressedKeys.contains(KeyCode.LEFT);
         boolean right = pressedKeys.contains(KeyCode.D) || pressedKeys.contains(KeyCode.RIGHT);

         if (left && !right) {
             player.moveLeft(delta);
         } else if (right && !left) {
             player.moveRight(delta);
         } else {
             player.stopMoving();
         }

         if (pressedKeys.contains(KeyCode.W) || pressedKeys.contains(KeyCode.UP)) {
             player.jump();
         }
     }

     private void updateTitle() {
        String status = weapon.isReloading() ? " [RELOADING]" : "";
        String ammo = weapon.getMagazine() + "/" + weapon.getMaxMagazine() +
                " | " + weapon.getCurrentAmmo() + "/" + weapon.getMaxAmmo();
        FXGL.getSettings().setTitle("MayhemArena - " + weapon.getType().getName() +
                " " + ammo + status);
    }

    public static void main(String[] args) {
        launch(args);
}}

