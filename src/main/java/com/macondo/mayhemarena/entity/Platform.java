package com.macondo.mayhemarena.entity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {
    private Entity entity;

    public Platform(double x, double y, double width, double height) {
        Rectangle shape = new Rectangle(width, height);
        shape.setFill(Color.GRAY);
        shape.setStroke(Color.DARKGRAY);
        shape.setStrokeWidth(2);

        entity = new Entity();
        entity.getViewComponent().addChild(shape);
        entity.setPosition(x, y);

        FXGL.getGameWorld().addEntity(entity);
    }

    public Entity getEntity() {
        return entity;
    }

    public double getX() {
        return entity.getX();
    }

    public double getY() {
        return entity.getY();
    }

    public double getWidth() {
        Rectangle rect = (Rectangle) entity.getViewComponent().getChildren().get(0);
        return rect.getWidth();
    }

    public double getHeight() {
        Rectangle rect = (Rectangle) entity.getViewComponent().getChildren().get(0);
        return rect.getHeight();
    }

}
