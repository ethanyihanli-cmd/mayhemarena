package com.macondo.mayhemarena;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.macondo.mayhemarena.entity.*;
import com.macondo.mayhemarena.config.ControlScheme;
import com.macondo.mayhemarena.config.PlayerAction;
import com.macondo.mayhemarena.map.MapLoader;
import com.macondo.mayhemarena.match.MatchController;
import com.macondo.mayhemarena.model.GameTheme;
import com.macondo.mayhemarena.model.PerkType;
import com.macondo.mayhemarena.ui.*;
import com.macondo.mayhemarena.util.SoundManager;
import com.macondo.mayhemarena.weapon.Weapon;
import com.macondo.mayhemarena.weapon.WeaponType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameApp extends GameApplication{
    private static final double PIT_DEATH_Y = 760;

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
    private Canvas backgroundCanvas;

    private MatchController matchController;
    private boolean matchStarted;

    private Set<KeyCode> pressedKeys;

    private WeaponType p1Weapon;
    private WeaponType p2Weapon;
    private String selectedMap;

    private PerkType p1Perk;
    private PerkType p2Perk;

    private ControlScheme p1Controls;
    private ControlScheme p2Controls;

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

        p1Controls = ControlScheme.playerOneDefault();
        p2Controls = ControlScheme.playerTwoDefault();
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
        Platform.runLater(() -> {
            createBackgroundCanvas();
            showMenusAndStartGame();
        });
    }

    private void showMenusAndStartGame() {
        SoundManager.getInstance().playBackgroundMusic("8-bit_-_crisis.mp3");

        MainMenu mainMenu = new MainMenu();
        boolean start = mainMenu.showAndWait();
        if (!start) {
            FXGL.getPrimaryStage().close();
            return;
        }

        SetupMenu setupMenu = new SetupMenu();
        SetupMenu.SetupResult setupResult = setupMenu.showAndWait();
        if (setupResult == null) {
            FXGL.getPrimaryStage().close();
            return;
        }
        selectedMap = setupResult.map;
        vsBot = setupResult.vsBot;

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

        ControlRemap controlRemap = new ControlRemap();
        ControlScheme[] controls = controlRemap.showAndWait();
        if (controls != null) {
            p1Controls = controls[0];
            p2Controls = controls[1];
        }

        mapLoader = new MapLoader();
        mapLoader.loadMap(selectedMap);

        double[] spawns = mapLoader.getSpawnPositions();
        player1 = new Player(1, spawns[0], spawns[1]);

        if (p1Perk != null) {
            player1.applyPerk(p1Perk);
        }


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
            public void onRoundEnd(int winnerId, int p1Wins, int p2Wins) {
                String text = "PLAYER " + winnerId + " WINS ROUND!";
                Color color = winnerId == 1 ? Color.LIME : Color.RED;
                matchMessage.show(text, color);
            }

            @Override
            public void onMatchEnd(int winnerId, int p1Wins, int p2Wins) {
                showMatchSummary();
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
            botWeapon = new Weapon(p2Weapon);
        } else {
            player2.reset();
            player2.setPosition(spawns[2], spawns[3]);
            weapon2 = new Weapon(p2Weapon);
        }

        weapon1 = new Weapon(p1Weapon);
        for (Bullet b : bullets) {
            b.deactivate();
        }
        bullets.clear();
    }

    private void setupInput() {
        Scene scene = FXGL.getPrimaryStage().getScene();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE && matchStarted) {
                ExitMenu exitMenu = new ExitMenu();
                ExitMenu.ExitAction action = exitMenu.showAndWait();

                switch (action) {
                    case RESUME:
                        break;
                    case MENU:
                        matchStarted = false;
                        FXGL.getPrimaryStage().close();
                        restartApp();
                        break;
                    case EXIT:
                        System.exit(0);
                        break;
                }
            }
            pressedKeys.add(e.getCode());

            if (e.getCode() == p1Controls.getReloadKey() && matchStarted && matchController.isRoundActive()) {
                weapon1.reload();
            }
            if (!vsBot && e.getCode() == p2Controls.getReloadKey() && matchStarted && matchController.isRoundActive()) {
                weapon2.reload();
            }

        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
             pressedKeys.remove(e.getCode());

             if (e.getCode() == p1Controls.getKey(PlayerAction.JUMP)) {
                  player1.releaseJump();
             }
             if (e.getCode() == p1Controls.getKey(PlayerAction.DOWN)) {
                 player1.releaseDown();
             }

             if (!vsBot && e.getCode() == p2Controls.getKey(PlayerAction.JUMP)) {
                 player2.releaseJump();
             }
             if (!vsBot && e.getCode() == p2Controls.getKey(PlayerAction.DOWN)) {
                 player2.releaseDown();
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
             bot.update(delta, mapLoader.getPlatforms(), player1.getX(), player1.getY());
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
                 bot.applyKnockback(b.getKnockback(), b.getX() > bot.getX() ? 1 : -1);
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
             if (player1.getY() > PIT_DEATH_Y) {
                 player1.takeDamage(player1.getHealth());
                 matchController.endRound(2);
             } else if (vsBot && bot.getY() > PIT_DEATH_Y) {
                 bot.takeDamage(bot.getHealth());
                 matchController.endRound(1);
             } else if (!vsBot && player2.getY() > PIT_DEATH_Y) {
                 player2.takeDamage(player2.getHealth());
                 matchController.endRound(1);
             } else if(player1.isKnockedOut()) {
                 matchController.endRound(2);
             } else if (vsBot && bot.isKnockedOut()) {
                 matchController.endRound(1);
             } else if (!vsBot && player2.isKnockedOut()) {
                 matchController.endRound(1);
             }
         }

         updateTitle();
    }

     private void handleInput(double delta) {
         boolean p1Left = pressedKeys.contains(p1Controls.getKey(PlayerAction.LEFT));
         boolean p1Right = pressedKeys.contains(p1Controls.getKey(PlayerAction.RIGHT));
         boolean p1Jump = pressedKeys.contains(p1Controls.getKey(PlayerAction.JUMP));
         boolean p1Down = pressedKeys.contains(p1Controls.getKey(PlayerAction.DOWN));
         boolean p1Shoot = pressedKeys.contains(p1Controls.getKey(PlayerAction.SHOOT));

         if (p1Left && !p1Right) {
             player1.moveLeft(delta);
         } else if (p1Right && !p1Left) {
             player1.moveRight(delta);
         } else {
             player1.stopMoving();
         }

         if (p1Jump) {
             player1.jump();
         }
         if (p1Down) {
             player1.down();
         }

         if (p1Shoot && matchStarted && matchController.isRoundActive()) {
             shoot(player1, weapon1);
         }

         if (!vsBot) {
             boolean p2Left = pressedKeys.contains(p2Controls.getKey(PlayerAction.LEFT));
             boolean p2Right = pressedKeys.contains(p2Controls.getKey(PlayerAction.RIGHT));
             boolean p2Jump = pressedKeys.contains(p2Controls.getKey(PlayerAction.JUMP));
             boolean p2Down = pressedKeys.contains(p2Controls.getKey(PlayerAction.DOWN));
             boolean p2Shoot = pressedKeys.contains(p2Controls.getKey(PlayerAction.SHOOT));

             if (p2Left && !p2Right) {
                 player2.moveLeft(delta);
             } else if (p2Right && !p2Left) {
                 player2.moveRight(delta);
             } else {
                 player2.stopMoving();
             }
             if (p2Jump) {
                 player2.jump();
             }
             if (p2Down) {
                 player2.down();
             }

             if (p2Shoot && matchStarted && matchController.isRoundActive()) {
                 shoot(player2, weapon2);
             }

         }

     }

    private void updateTitle() {
        String info = selectedMap + " | Round " + matchController.getRoundNumber() +
                " | P1: " + matchController.getPlayer1Wins() + " - " + matchController.getPlayer2Wins() + " P2";
        FXGL.getPrimaryStage().setTitle(info);
    }

    private void renderBackground() {
        if (backgroundCanvas == null) {
            return;
        }
        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();
        BackgroundRenderer.draw(gc, theme);
    }

    private void createBackgroundCanvas() {
        backgroundCanvas = new Canvas(1280, 720);
        com.almasb.fxgl.entity.Entity background = new com.almasb.fxgl.entity.Entity();
        background.getViewComponent().addChild(backgroundCanvas);
        background.setPosition(0, 0);
        FXGL.getGameWorld().addEntity(background);
        renderBackground();
    }

    private void showMatchSummary() {
        String text = "MATCH OVER";
        String subText = "Player 1: " + matchController.getPlayer1Wins() +
                         " - Player 2: " + matchController.getPlayer2Wins() +
                         " | Rounds: " + matchController.getRoundNumber();
        Color color = matchController.getPlayer1Wins() > matchController.getPlayer2Wins()
                ? Color.LIME : Color.RED;

        matchMessage.showMatchResult(text, subText, color);

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Platform.runLater(() -> {
                    if (matchMessage.isVisible()) {
                        matchMessage.hide();
                    }
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void restartApp() {
        FXGL.getGameController().exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

