package com.macondo.mayhemarena.entity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.macondo.mayhemarena.model.PerkType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Bot {
    public static final int WIDTH = 34;
    public static final int HEIGHT = 58;

    private double x;
    private double y;
    private double dx;
    private double dy;
    private boolean grounded;
    private boolean jumping;
    private int facing;
    private int health;
    private int maxHealth;
    private boolean knockedOut;
    private PerkType perk;
    private double speedMultiplier;
    private double knockbackResist;
    private Entity entity;
    private Rectangle body;
    private Rectangle hat;
    private Rectangle visor;
    private Rectangle gun;
    private double stateTimer;
    private String state;
    private int difficulty;

    public Bot(double startX, double startY, int difficulty) {
        this.x = startX;
        this.y = startY;
        this.difficulty = difficulty;
        dx = 0;
        dy = 0;
        grounded = false;
        jumping = false;
        facing = 1;
        state = "idle";
        stateTimer = 0;
        health = 100;
        maxHealth = 100;
        knockedOut = false;
        perk = null;
        speedMultiplier = 1.0;
        knockbackResist = 1.0;

        createEntity();
        }

        private void createEntity() {
            body = new Rectangle(WIDTH, HEIGHT - 10);
            body.setArcWidth(8);
            body.setArcHeight(8);
            body.setFill(Color.PURPLE);
            body.setStroke(Color.BLACK);
            body.setStrokeWidth(2);

            hat = new Rectangle(WIDTH - 6, 12);
            hat.setTranslateX(3);
            hat.setTranslateY(-12);
            hat.setArcWidth(6);
            hat.setArcHeight(6);
            hat.setFill(Color.DARKVIOLET);

            visor = new Rectangle(10, 6);
            visor.setTranslateX(WIDTH / 2 - 5);
            visor.setTranslateY(20);
            visor.setArcWidth(4);
            visor.setArcHeight(4);
            visor.setFill(Color.LIGHTBLUE);

            gun = new Rectangle(18, 4);
            gun.setTranslateX(WIDTH - 4);
            gun.setTranslateY(26);
            gun.setArcWidth(4);
            gun.setArcHeight(4);
            gun.setFill(Color.DARKGRAY);

            entity = new Entity();
            entity.getViewComponent().addChild(body);
            entity.getViewComponent().addChild(hat);
            entity.getViewComponent().addChild(visor);
            entity.getViewComponent().addChild(gun);
            entity.setPosition(x, y);

            FXGL.getGameWorld().addEntity(entity);
        }

        public void update(double delta, List<Platform> platforms, double playerX, double playerY) {
            if (knockedOut) return;

            stateTimer += delta;
            makeDecision(delta, playerX, playerY);

            dy += 1900 * delta;
            if (dy > 1200) dy = 1200;

            x += dx * delta;
            if (x < 0) x = 0;
            if (x + WIDTH > 1280) x = 1280 - WIDTH;

            y += dy * delta;
            grounded = false;

            for (Platform plat : platforms) {
                double px = plat.getX();
                double py = plat.getY();
                double pw = plat.getWidth();
                double ph = plat.getHeight();

                if (x + WIDTH > px && x < px + pw) {
                    if (dy >= 0 && y + HEIGHT >= py && y + HEIGHT <= py + ph + 10) {
                        y = py - HEIGHT;
                        dy = 0;
                        grounded = true;
                        jumping = false;
                    }
                }
            }

            entity.setScaleX(facing);
            entity.setPosition(x, y);
            updateGun();
        }

        private void makeDecision(double delta, double playerX, double playerY) {
            double distance = Math.abs(playerX - x);
            double moveSpeed = (180 + difficulty * 40) * speedMultiplier;
            double jumpChance = 0.02 + difficulty * 0.01;

            if (stateTimer > 0.5 + Math.random() * 0.5) {
                stateTimer = 0;
                if (distance < 200) {
                    state = Math.random() < 0.5 ? "retreat" : "attack";
                } else if (distance < 500) {
                    state = Math.random() < 0.6 ? "approach" : "attack";
                } else {
                    state = "approach";
                }
            }

            switch (state) {
                case "approach":
                    if (playerX > x) { dx = moveSpeed; facing = 1; }
                    else { dx = -moveSpeed; facing = -1; }
                    if (Math.random() < jumpChance && grounded) jump();
                    break;
                case "retreat":
                    if (playerX > x) { dx = -moveSpeed; facing = -1; }
                    else { dx = moveSpeed; facing = 1; }
                    break;
                case "attack":
                    dx = 0;
                    if (Math.random() < jumpChance * 2 && grounded) jump();
                    break;
                default:
                    dx = 0;
                    break;
            }
            updateGun();
        }

        public void jump() {
            if (!grounded || jumping || knockedOut) return;
            dy = -760;
            grounded = false;
            jumping = true;
        }

        public void releaseJump() { jumping = false; }

        public void applyPerk(PerkType perkType) {
            this.perk = perkType;
            switch (perkType) {
                case SPEED_BOOST:
                    speedMultiplier = 1.2;
                    break;
                case KNOCKBACK_RESIST:
                    knockbackResist = 0.6;
                    break;
                case HEALTH_BOOST:
                    maxHealth = 130;
                    health = maxHealth;
                    break;
                default:
                    break;
            }
        }

        private void updateGun() {
            gun.setTranslateX(facing == 1 ? WIDTH - 4 : -14);
        }

        public void takeDamage(int amount) {
            if (knockedOut) return;
            health -= amount;
            if (health <= 0) { health = 0; knockedOut = true; body.setFill(Color.DARKRED); }
        }

        public void applyKnockback(int force, int direction) {
            if (knockedOut) return;
            double resist = 1.0 - (1.0 - knockbackResist) * 0.6;
            double knock = grounded ? force * 0.3 * resist : force * 0.5 * resist;
            dx += direction * knock;
            dy = -120;
            if (dx > 800) dx = 800;
            if (dx < -800) dx = -800;
        }

        public void reset() {
            health = maxHealth;
            knockedOut = false;
            dx = 0;
            dy = 0;
            body.setFill(Color.PURPLE);
        }

        public void setPosition(double nx, double ny) { x = nx; y = ny; entity.setPosition(x, y); }

        public boolean isKnockedOut() { return knockedOut; }
        public int getHealth() { return health; }
        public int getMaxHealth() { return maxHealth; }
        public double getX() { return x; }
        public double getY() { return y; }
        public int getFacing() { return facing; }
        public Entity getEntity() { return entity; }
        public boolean isReadyToShoot() { return state.equals("attack") && Math.random() < 0.3; }
}
