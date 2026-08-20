package com.macondo.mayhemarena;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.macondo.mayhemarena.entity.*;
import com.macondo.mayhemarena.map.MapLoader;
import com.macondo.mayhemarena.match.MatchController;
import com.macondo.mayhemarena.model.GameTheme;
import com.macondo.mayhemarena.model.PerkType;
import com.macondo.mayhemarena.ui.BackgroundRenderer;
import com.macondo.mayhemarena.ui.HUD;
import com.macondo.mayhemarena.ui.MatchMessage;
import com.macondo.mayhemarena.weapon.Weapon;
import com.macondo.mayhemarena.weapon.WeaponType;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
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
    private Bot bot;
    private boolean vsBot;

    private Weapon weapon1;
    private Weapon weapon2;
    private Weapon botWeapon;

    private MapLoader mapLoader;
    private List<Bullet> bullets;

    private HUD hud;
    private MatchMessage matchMessage;
    private GameTheme theme;

    private MatchController matchController;
    private boolean matchStarted;

    private Set<KeyCode> pressedKeys;

    private WeaponType p1Weapon;
    private WeaponType p2Weapon;
    private String selectedMap;

    private PerkType p1Perk;
    private PerkType p2Perk;

    public GameApp() {
        pressedKeys = new HashSet<>();
        bullets = new ArrayList<>();
        matchStarted = false;
        vsBot = true;
        theme = GameTheme.arenaTheme();
        matchController = new MatchController();
        selectedMap = "Sky Ruins";
        p1Weapon = WeaponType.PISTOL;
        p2Weapon = WeaponType.PISTOL;

        p1Perk = null;
        p2Perk = null;
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
        MainMenu mainMenu = new MainMenu();
        boolean start = mainMenu.showAndWait();
        if (!start) {
            FXGL.getPrimaryStage().close();
            return;
        }

        MapSelection mapSelect = new MapSelection();
        String map = mapSelect.showAndWait();
        if (map != null) {
            selectedMap = map;
        }

        WeaponSelection weaponSelect = new WeaponSelection();
        WeaponType[] weapons = weaponSelect.showAndWait();
        if (weapons != null) {
            p1Weapon = weapons[0];
            p2Weapon = weapons[1];
        }

        PerkSelection perkSelect = new PerkSelection();
        PerkType[] perks = perkSelect.showAndWait();
        if (perks != null) {
            p1Perk = perks[0];
            p2Perk = perks[1];
        }

        mapLoader = new MapLoader();
        mapLoader.loadMap(selectedMap);

        double[] spawns = mapLoader.getSpawnPositions();
        player1 = new Player(1, spawns[0], spawns[1]);

        if (p1Perk != null) {
            player1.applyPerk(p1Perk);
        }

        vsBot = true;

        if (vsBot) {
            bot = new Bot(spawns[2], spawns[3], 1);
            if (p2Perk != null) {
                bot.applyPerk(p2Perk);
            }
            botWeapon = new Weapon(p2Weapon);
        } else {
            player2 = new Player(2, spawns[2], spawns[3]);
            if (p2Perk != null) {
                player2.applyPerk(p2Perk);
            }
            weapon2 = new Weapon(p2Weapon);
        }

        weapon1 = new Weapon(p1Weapon);

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
            }

            @Override
            public void onMatchEnd(Player winner, int p1Wins, int p2Wins) {
                String text = "PLAYER" + winner.getPlayerId() + " WINS THE MATCH!";
                matchMessage.show(text, Color.GOLD);
                matchStarted = false;
            }
        });

        matchStarted = true;
        matchController.startMatch();

        setupInput();
    }

    private void resetPlayers() {
        double[] spawns = mapLoader.getSpawnPositions();
        player1.reset();
        player1.setPosition(spawns[0], spawns[1]);

        if (vsBot) {
            bot.reset();
            bot.setPosition(spawns[2], spawns[3]);
            botWeapon = new Weapon(WeaponType.PISTOL);
        } else {
            player2.reset();
            player2.setPosition(spawns[2], spawns[3]);
            weapon2 = new Weapon(WeaponType.PISTOL);
        }

        weapon1 = new Weapon(WeaponType.PISTOL);
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
            }
            if (!vsBot && e.getCode() == KeyCode.M && matchStarted && matchController.isRoundActive()) {
                shoot(player2, weapon2);
            }
            if (e.getCode() == KeyCode.R && matchStarted && matchController.isRoundActive()) {
                weapon1.reload();
            }
            if (!vsBot && e.getCode() == KeyCode.COMMA && matchStarted && matchController.isRoundActive()) {
                weapon2.reload();
            }
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
             pressedKeys.remove(e.getCode());

             if (e.getCode() == KeyCode.W) {
                  player1.releaseJump();
             }

             if (!vsBot && e.getCode() == KeyCode.UP) {
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

        double recoilForce = weapon.getType().getRecoil() * 10;
        player.applyKnockback((int) recoilForce, -facing);
    }

    @Override
    protected void onUpdate(double delta) {
        renderBackground();

        if (!matchStarted) {
            return;
        }

         handleInput(delta);

         player1.update(delta, mapLoader.getPlatforms());
         if (vsBot) {
             bot.update(delta, mapLoader.getPlatform(), player1.getX(), player1.getY());
             if (bot.isReadyToShoot() && botWeapon.canShoot()) {
                 double bx = bot.getX();
                 double by = bot.getY();
                 int bf = bot.getFacing();
                 List<Bullet> botBullets = botWeapon.shoot(bx, by, bf, 99);
                 bullets.addAll(botBullets);
             }
         } else {
             player2.update(delta, mapLoader.getPlatforms());
         }

         weapon1.update(delta);
         if (vsBot) {
             botWeapon.update(delta);
         } else {
             weapon2.update(delta);
         }

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

             if (vsBot && b.hitsBot(bot)) {
                 bot.takeDamage(b.getDamage());
                 bot.applyKnockback(b.getKnockback(), b.getX() > player2.getX() ? 1 : -1);
                 b.deactivate();
                 toRemove.add(b);
                 continue;
             }

             if (!vsBot && b.hitsPlayer(player2)) {
                 player2.takeDamage(b.getDamage());
                 player2.applyKnockback(b.getKnockback(), b.getX() > player2.getX() ? 1 : -1);
                 b.deactivate();
                 toRemove.add(b);
                 continue;
             }
         }

         bullets.removeAll(toRemove);

         if (vsBot) {
             hud.update(player1, bot, weapon1, botWeapon);
         } else {
             hud.update(player1, player2, weapon1, weapon2);
         }

         if (matchController.isRoundActive()) {
             if(player1.isKnockedOut()) {
                 matchController.endRound(vsBot ? bot : player2);
             } else if (vsBot && bot.isKnockedOut()) {
                 matchController.endRound(player1);
             } else if (!vsBot && player2.isKnockedOut()) {
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

         if (pressedKeys.contains(KeyCode.W)) {
             player1.jump();
         }

         if (!vsBot) {
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

     }

     private void updateTitle() {
        String info = selectedMap + " | Round " + matchController.getRoundNumber() +
                " | P1: " + matchController.getPlayer1Wins() + " - " + matchController.getPlayer2Wins() + " P2";
        FXGL.getSettings().setTitle(info);
    }

    private void renderBackground() {
        GraphicsContext gc = FXGL.getGameScene().getViewport().getGraphicsContext2D();
        BackgroundRenderer.draw(gc, theme);
    }

    private String selectedMap = "Sky Ruins";

    public static void main(String[] args) {
        launch(args);
    }
}

