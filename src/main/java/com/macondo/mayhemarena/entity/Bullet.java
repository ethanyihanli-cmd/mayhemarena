package com.macondo.mayhemarena.entity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.effect.DropShadow;
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
    private double life;
    private double width;
    private double height;
    private double distanceTraveled;
    private boolean active;
    private int shooterId;

    public Bullet(double startX, double startY, int facing, int damage, int knockback, int range, int shooterId, double spread) {
        this(startX, startY, facing, damage, knockback, range, shooterId, spread, 800, 14, 6, 3, Color.YELLOW);
    }

    public Bullet(double startX, double startY, int facing, int damage, int knockback, int range, int shooterId,
                  double spread, double speed, double width, double height, double life, Color color) {
        this.x = startX + (facing == 1 ? 20 : -20);
        this.y = startY + 20;
        this.dx = facing * speed;
        this.dy = spread;
        this.damage = damage;
        this.knockback = knockback;
        this.range = range;
        this.life = life;
        this.width = width;
        this.height = height;
        this.distanceTraveled = 0;
        this.active = true;
        this.shooterId = shooterId;

        Rectangle shape = new Rectangle(width, height);
        shape.setFill(color);
        shape.setArcWidth(4);
        shape.setArcHeight(4);
        shape.setStroke(Color.color(1, 1, 1, 0.7));
        shape.setStrokeWidth(1);
        shape.setEffect(new DropShadow(8, Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.5)));

        entity = new Entity();
        entity.getViewComponent().addChild(shape);
        entity.setPosition(x, y);
        entity.setRotation(dx == 0 && dy == 0 ? 0 : Math.toDegrees(Math.atan2(dy, dx)));

        FXGL.getGameWorld().addEntity(entity);

    }

    public void update(double delta) {
        if (!active) return;
        x += dx * delta;
        y += dy * delta;
        distanceTraveled += Math.abs(dx * delta);
        life -= delta;
        entity.setPosition(x, y);

        if (life <= 0 || x < -50 || x > 1330 || y < -50 || y > 770 || distanceTraveled > range) {
            active = false;
            FXGL.getGameWorld().removeEntity(entity);
        }
    }

    public boolean hitsPlayer(Player player) {
        if (player.getPlayerId() == shooterId) return false;
        if (player.isKnockedOut()) return false;
        double px = player.getX();
        double py = player.getY();
        return x + width > px && x < px + Player.WIDTH && y + height > py && y < py + Player.HEIGHT;
    }

    public boolean hitsBot(Bot bot) {
        if (bot.isKnockedOut()) return false;
        double px = bot.getX();
        double py = bot.getY();
        return x + width > px && x < px + Bot.WIDTH && y + height > py && y < py + Bot.HEIGHT;
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
