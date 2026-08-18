package com.macondo.mayhemarena;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.macondo.mayhemarena.entity.Bullet;
import com.macondo.mayhemarena.entity.Player;
import com.macondo.mayhemarena.map.MapLoader;
import com.macondo.mayhemarena.match.MatchController;
import com.macondo.mayhemarena.ui.HUD;
import com.macondo.mayhemarena.ui.MapSelection;
import com.macondo.mayhemarena.ui.MatchMessage;
import com.macondo.mayhemarena.ui.WeaponSelection;
import com.macondo.mayhemarena.util.SoundManager;
import com.macondo.mayhemarena.weapon.Weapon;
import com.macondo.mayhemarena.weapon.WeaponType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

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
    private MatchController matchController;
    private MatchMessage matchMessage;
    private SoundManager sound;

    private WeaponType p1Weapon;
    private WeaponType p2Weapon;
    private String selectedMap;
    private boolean matchStarted;

    public GameApp() {
        pressedKeys = new HashSet<>();
        bullets = new ArrayList<>();
        matchStarted = false;
        selectedMap = "Sky Ruins";
        matchController = new MatchController();
        sound = SoundManager.getInstance();
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
        matchMessage = new MatchMessage();

        FXGL.getPrimaryStage().getScene().setRoot(
                new javafx.scene.layout.StackPane(
                        FXGL.getPrimaryStage().getScene().getRoot(),
                        matchMessage.getContainer()
                )
        );

        matchController.setListener(new MatchController.MatchStateListener() {
            @Override
            public void onRoundStart(int roundNumber) {
                matchMessage.show("ROUND" + roundNumber, Color.GOLD);
                resetPlayers();
                bullets.clear();
            }

            @Override
            public void onRoundEnd(Player winner, int p1Wins, int p2Wins) {
                String text = "PLAYER " + winner.getPlayerId() + " WINS ROUND!";
                Color color = winner.getPlayerId() == 1 ? Color.LIME : Color.RED;
                matchMessage.show(text, color);
                sound.playWin();
            }

            @Override
            public void onMatchEnd(Player winner, int p1Wins, int p2Wins) {
                String text = "PLAYER" + winner.getPlayerId() + " WINS THE MATCH!";
                matchMessage.show(text, Color.GOLD);
                sound.playWin();
                matchStarted = false;
            }
        });

        matchStarted = true;
        matchController.startMatch();

        setupInput();

        System.out.println("Map: " + selectedMap);
        System.out.println("Player 1: " + p1Weapon.getName());
        System.out.println("Player 2: " + p2Weapon.getName());


        double[] spawns = mapLoader.getSpawnPositions();
        player1 = new Player(1, spawns[0], spawns[1]);
        player2 = new Player(2, spawns[2], spawns[3]);

    }

    private void resetPlayers() {
        double[] spawns = mapLoader.getSpawnPositions();
        player1.reset();
        player2.reset();
        player1.setPosition(spawns[0], spawns[1]);
        player2.setPosition(spawns[2], spawns[3]);

        weapon1 = new Weapon(p1Weapon);
        weapon2 = new Weapon(p2Weapon);

        for (Bullet b : bullets) {
            b.deactivate();
        }
        bullets.clear();
    }

    private void setupInput() {
        Scene scene = FXGL.getPrimaryStage().getScene();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            pressedKeys.add(e.getCode());

            if (e.getCode() == KeyCode.SPACE && matchStarted && matchController.isRoundActive()) {
                shoot(player1, weapon1);
                sound.playShoot();
            }
            if (e.getCode() == KeyCode.M && matchStarted && matchController.isRoundActive()) {
                shoot(player2, weapon2);
                sound.playShoot();
            }
            if (e.getCode() == KeyCode.R && matchStarted && matchController.isRoundActive()) {
                weapon1.reload();
                sound.playReload();
            }
            if (e.getCode() == KeyCode.COMMA && matchStarted && matchController.isRoundActive()) {
                weapon2.reload();
                sound.playReload();
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
        if (!matchController.isRoundActive()) {
            return;
        }
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
                 sound.playHit();
                 continue;
             }

             if (b.hitsPlayer(player2)) {
                 player2.takeDamage(b.getDamage());
                 player2.applyKnockback(b.getKnockback(), b.getX() > player2.getX() ? 1 : -1);
                 b.deactivate();
                 toRemove.add(b);
                 sound.playHit();
                 continue;
             }
         }

         bullets.removeAll(toRemove);

         hud.update(player1, player2, weapon1, weapon2);

         if (matchController.isRoundActive()) {
             if(player1.isKnockedOut()) {
                 matchController.endRound(player2);
             } else if (player2.isKnockedOut()) {
                 matchController.endRound(player1);
             }
         }

         updateTitle();
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
        FXGL.getSettings().setTitle(selectedMap + " | Round " + matchController.getRoundNumber() +
                " | P1: " + matchController.getPlayer1Wins() + " - " + matchController.getPlayer2Wins() + " P2");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

