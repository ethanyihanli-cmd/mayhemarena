package com.macondo.mayhemarena;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.macondo.mayhemarena.entity.Player;
import com.macondo.mayhemarena.map.MapLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class GameApp extends GameApplication{

    private Player player;
    private MapLoader mapLoader;
    private Set<KeyCode> pressedKeys;

    public GameApp() {
        pressedKeys = new HashSet<>();
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
        setupInput();
    }

    private void setupInput() {
        Scene scene = FXGL.getPrimaryStage().getScene();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            pressedKeys.add(e.getCode());
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
             pressedKeys.remove(e.getCode());
             if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
                  player.releaseJump();
             }
        });
    }

    @Override
    protected void onUpdate(double delta) {
         handleInput(delta);
         player.update(delta);
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

    public static void main(String[] args) {
        launch(args);
}}

