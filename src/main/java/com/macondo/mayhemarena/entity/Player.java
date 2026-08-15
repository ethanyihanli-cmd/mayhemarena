package com.macondo.mayhemarena.entity;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {

    public static final int WIDTH = 34;
    public static final int HEIGHT = 58;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private boolean grounded;
    private boolean jumping;
    private int facing;

    private Entity entity;
    private Rectangle body;
    private Rectangle hat;
    private Rectangle visor;
    private Rectangle gun;

    public Player() {
        x = 640;
        y = 360;
        dx = 0;
        dy = 0;
        grounded = false;
        jumping = false;
        facing = 1;

        createEntity();
        FXGL.getGameWorld().addEntity(entity);
    }

    private void createEntity() {
        body = new Rectangle(WIDTH,HEIGHT - 10);
        body.setArcWidth(8);
        body.setArcHeight(8);
        body.setFill(Color.GREEN);
        body.setStroke(Color.BLACK);
        body.setStrokeWidth(2);

        hat = new Rectangle(WIDTH - 6,12);
        hat.setTranslateX(3);
        hat.setTranslateY(-12);
        hat.setArcWidth(6);
        hat.setArcHeight(6);
        hat.setFill(Color.DARKBLUE);

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
        dx = -320;
        facing = -1;
        updateGun();
     }

     public void moveRight(double delta) {
        dx = 320;
        facing = 1;
        updateGun();
     }

     public void stopMoving() {
        dx = 0;
     }

     public void jump() {
        if (!grounded || jumping) {
            return;
        }
        dy = -760;
        grounded = false;
        jumping = true;
     }

     public void releaseJump() {
        jumping = false;
    }

     private void updateGun() {
         if (facing == 1) {
             gun.setTranslateX(WIDTH - 4);
         } else {
             gun.setTranslateX(-14);
         }
     }

     public void update(double delta) {
         dy += 1900 * delta;
         if (dy > 1200) {
             dy = 1200;
         }

         x += dx * delta;
         y += dy * delta;

         if (x < 0) {
             x = 0;
         }
         if (x + WIDTH > 1280) {
             x = 1280 - WIDTH;
         }

         if (y + HEIGHT > 600) {
              y = 600 - HEIGHT;
              dy = 0;
              grounded = true;
              jumping = false;
         } else {
             grounded = false;
         }

         entity.setScaleX(facing);
         entity.setPosition(x, y);
         updateGun();
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
     public boolean isOnGround() {
        return grounded;
     }
     public Entity getEntity() {
        return entity;
     }
}