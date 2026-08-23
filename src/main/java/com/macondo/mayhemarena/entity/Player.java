package com.macondo.mayhemarena.entity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.macondo.mayhemarena.model.PerkType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Player {
    public static final int WIDTH = 34;
    public static final int HEIGHT = 58;

    private int playerId;
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
    private int jumpCount;
    private int maxJumps;
    private int midAirJumpsLeft;
    private double speedMultiplier;
    private double knockbackResist;
    private boolean pressingDown;
    private double dropPlatformTimer;

    private Entity entity;
    private Rectangle body;
    private Rectangle hat;
    private Rectangle visor;
    private Rectangle gun;

    public Player(int playerId, double startX, double startY) {
        this.playerId = playerId;
        this.x = startX;
        this.y = startY;
        dx = 0;
        dy = 0;
        grounded = false;
        jumping = false;
        facing = 1;

        perk = null;
        maxJumps = 1;
        jumpCount = 0;
        midAirJumpsLeft = 0;
        speedMultiplier = 1.0;
        knockbackResist = 1.0;
        pressingDown = false;
        dropPlatformTimer = 0;

        health = 100;
        maxHealth = 100;
        knockedOut = false;

        createEntity();
        FXGL.getGameWorld().addEntity(entity);
    }

    public void applyPerk(PerkType perkType) {
        this.perk = perkType;

        switch (perkType) {
            case DOUBLE_JUMP:
                maxJumps = 2;
                break;
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

    private void createEntity() {
        body = new Rectangle(WIDTH,HEIGHT - 10);
        body.setArcWidth(8);
        body.setArcHeight(8);

        if (playerId == 1) {
            body.setFill(Color.GREEN);
        } else {
            body.setFill(Color.RED);
        }

        body.setStroke(Color.BLACK);
        body.setStrokeWidth(2);

        hat = new Rectangle(WIDTH - 6,12);
        hat.setTranslateX(3);
        hat.setTranslateY(-12);
        hat.setArcWidth(6);
        hat.setArcHeight(6);

        if (playerId == 1) {
            hat.setFill(Color.DARKBLUE);
        } else {
            hat.setFill(Color.DARKRED);
        }

        visor = new Rectangle(10,6);
        visor.setTranslateX(WIDTH / 2 - 5);
        visor.setTranslateY(20);
        visor.setArcWidth(4);
        visor.setArcHeight(4);
        visor.setFill(Color.LIGHTBLUE);

        gun = new Rectangle(18,4);
        gun.setTranslateX(WIDTH - 4);
        gun.setTranslateY(26);
        gun.setArcHeight(4);
        gun.setArcWidth(4);
        gun.setFill(Color.DARKGRAY);

        entity = new Entity();
        entity.getViewComponent().addChild(body);
        entity.getViewComponent().addChild(hat);
        entity.getViewComponent().addChild(visor);
        entity.getViewComponent().addChild(gun);
        entity.setPosition(x,y);
    }

     public void moveLeft(double delta) {
        dx = -320 * speedMultiplier;
        facing = -1;
        updateGun();
     }

     public void moveRight(double delta) {
        dx = 320 * speedMultiplier;
        facing = 1;
        updateGun();
     }

     public void stopMoving() {
        dx *= grounded ? 0.35 : 0.92;
        if (Math.abs(dx) < 18) {
            dx = 0;
        }
     }

     public void jump() {
        if (jumping) {
            return;
        }

        if (grounded) {
            dy = -760;
            grounded = false;
            jumping = true;
        } else if (midAirJumpsLeft > 0) {
            dy = -720;
            midAirJumpsLeft--;
            jumping = true;
        }
     }

     public void releaseJump() {
        jumping = false;
    }

     public void down() {
        if (pressingDown) {
            return;
        }
        if (grounded) {
            dropPlatformTimer = 0.22;
            y += 10;
            dy = 220;
            grounded = false;
        } else {
            dy = Math.max(dy, 980);
        }
        pressingDown = true;
     }

     public void releaseDown() {
        pressingDown = false;
     }

     private void updateGun() {
         if (facing == 1) {
             gun.setTranslateX(WIDTH - 4);
         } else {
             gun.setTranslateX(-14);
         }
     }

     public void takeDamage(int amount) {
        if (knockedOut) {
            return;
        }

        health -= amount;
        if (health <= 0) {
            health = 0;
            knockedOut = true;
            body.setFill(Color.DARKRED);
            System.out.println("Player " + playerId + " is knocked out!");
        }
     }

     public void applyKnockback(int force, int direction) {
        if (knockedOut) {
            return;
        }

        double resist = 1.0 - (1.0 - knockbackResist) * 0.6;

        double knockbackForce = grounded ? force * 0.3 * resist : force * 0.5 * resist;

        dx += direction * knockbackForce;
        dy = -100;

        if (dx > 800) dx = 800;
        if (dx < -800) dx = -800;
     }

     public void reset() {
        health = maxHealth;
        knockedOut = false;
        dx = 0;
        dy = 0;
        jumpCount = 0;
        midAirJumpsLeft = Math.max(0, maxJumps - 1);
        pressingDown = false;
        dropPlatformTimer = 0;

        if (playerId == 1) {
            body.setFill(Color.GREEN);
        } else {
            body.setFill(Color.RED);
        }

        x = (playerId == 1) ? 500 : 780;
        y = 360;
     }

     public void update(double delta, List<Platform> platforms) {
        if (knockedOut) {
            return;
        }

         if (dropPlatformTimer > 0) {
             dropPlatformTimer -= delta;
         }

         double oldY = y;

         dy += 1900 * delta;
         if (dy > 1200) {
             dy = 1200;
         }

         x += dx * delta;

         if (x < 0) {
             x = 0;
         }
         if (x + WIDTH > 1280) {
             x = 1280 - WIDTH;
         }

         y += dy * delta;

         grounded = false;

         for (Platform plat : platforms) {
             if (dropPlatformTimer > 0 && plat.canDrop()) {
                 continue;
             }

             boolean alignX = x + WIDTH - 6 > plat.getLeft() && x + 6 < plat.getRight();
             boolean wasHigher = oldY + HEIGHT <= plat.getTop() + 4;
             boolean nowLower = y + HEIGHT >= plat.getTop();
             double collisionBuffer = Math.max(14, dy * delta + 8);
             boolean hitTop = nowLower && oldY + HEIGHT <= plat.getTop() + collisionBuffer;

             if (alignX && wasHigher && hitTop && dy >= 0) {
                     y = plat.getTop() - HEIGHT;
                     dy = 0;
                     grounded = true;
                     jumping = false;
                     midAirJumpsLeft = Math.max(0, maxJumps - 1);
             }
         }

         entity.setScaleX(facing);
         entity.setPosition(x, y);
         updateGun();
     }

     public void setPosition(double newX, double newY) {
        this.x = newX;
        this.y = newY;
        entity.setPosition(x, y);
     }

     public int getPlayerId() {
        return playerId;
     }

     public double getX() {
        return x;
     }
     public double getY() {
        return y;
     }
     public int getFacing() {
        return facing;
     }
     public boolean isGrounded() {
        return grounded;
     }
     public Entity getEntity() {
        return entity;
     }
     public int getHealth() {
        return health;
     }
     public int getMaxHealth() {
        return maxHealth;
     }
     public boolean isKnockedOut() {
        return knockedOut;
     }
     public PerkType getPerk() { return perk; }
}
