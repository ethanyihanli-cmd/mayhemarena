package com.macondo.mayhemarena.entity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet {
    private Entity entity;
    private double x;
    private double y;
    private double dx;
    private double dy;
    private int damage;
    private int knockback;
    private int range;
    private double distanceTraveled;
    private boolean active;
    private int shooterId;

    public Bullet(double startX, double startY, int facing, int damage, int knockback, int range, int shooterId, double spread) {
        this.x = startX + (facing == 1 ? 20 : -20);
        this.y = startY + 20;
        this.dx = facing * 800;
        this.dy = spread * 800;
        this.damage = damage;
        this.knockback = knockback;
        this.range = range;
        this.distanceTraveled = 0;
        this.active = true;
        this.shooterId = shooterId;

        Rectangle shape = new Rectangle(12, 6);
        shape.setFill(Color.YELLOW);
        shape.setArcWidth(4);
        shape.setArcHeight(4);
        shape.setStroke(Color.ORANGE);
        shape.setStrokeWidth(1);

        entity = new Entity();
        entity.getViewComponent().addChild(shape);
        entity.setPosition(x, y);

        FXGL.getGameWorld().addEntity(entity);

    }

    public void update(double delta) {
        if (!active) {
            return;
        }

        x += dx * delta;
        y += dy * delta;
        distanceTraveled += Math.abs(dx * delta);

        entity.setPosition(x, y);

        if (x < -50 || x > 1330 || distanceTraveled > range) {
            active = false;
            FXGL.getGameWorld().removeEntity(entity);
        }
    }

    public boolean hitsPlayer(Player player) {
        if (player.getPlayerId() == shooterId) {
            return false;
        }

        if (player.isKnockedOut()) {
            return false;
        }

        double px = player.getX();
        double py = player.getY();
        double pw = Player.WIDTH;
        double ph = Player.HEIGHT;

        return x > px && x < px + pw && y > py && y < py + ph;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getDamage() { return damage; }
    public int getKnockback() { return knockback; }
    public boolean isActive() { return active; }
    public Entity getEntity() { return entity; }
    public int getShooterId() { return shooterId; }

    public void deactivate() {
        active = false;
        FXGL.getGameWorld().removeEntity(entity);
    }

}
