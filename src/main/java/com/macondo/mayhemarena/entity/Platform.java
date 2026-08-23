package com.macondo.mayhemarena.entity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {
    private Entity entity;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final boolean canDrop;

    public Platform(double x, double y, double width, double height) {
        this(x, y, width, height, true, Color.GRAY);
    }

    public Platform(double x, double y, double width, double height, boolean canDrop, Color fill) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.canDrop = canDrop;

        Rectangle shape = new Rectangle(width, height);
        shape.setArcWidth(8);
        shape.setArcHeight(8);
        shape.setFill(fill);
        shape.setStroke(canDrop ? Color.web("#d7f0ff") : Color.web("#5f4b32"));
        shape.setStrokeWidth(2);
        shape.setEffect(new DropShadow(10, Color.color(0, 0, 0, 0.18)));

        Rectangle top = new Rectangle(width, Math.min(8, height));
        top.setFill(canDrop ? Color.web("#a7f3d0") : Color.web("#8fd14f"));
        top.setTranslateY(-2);

        entity = new Entity();
        entity.getViewComponent().addChild(shape);
        entity.getViewComponent().addChild(top);
        entity.setPosition(x, y);

        FXGL.getGameWorld().addEntity(entity);
    }

    public Entity getEntity() {
        return entity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getTop() {
        return y;
    }

    public double getLeft() {
        return x;
    }

    public double getRight() {
        return x + width;
    }

    public boolean canDrop() {
        return canDrop;
    }

    public void remove() {
        entity.removeFromWorld();
    }

}
