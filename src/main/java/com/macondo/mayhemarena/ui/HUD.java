package com.macondo.mayhemarena.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.macondo.mayhemarena.entity.Bot;
import com.macondo.mayhemarena.entity.Player;
import com.macondo.mayhemarena.weapon.Weapon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class HUD {
    private Entity entity;
    private Rectangle p1HealthBar;
    private Rectangle p1HealthFill;
    private Rectangle p2HealthBar;
    private Rectangle p2HealthFill;
    private Text p1AmmoText;
    private Text p2AmmoText;
    private Text p1WeaponText;
    private Text p2WeaponText;

    private double p1FlashTimer = 0;
    private double p2FlashTimer = 0;

    public HUD() {
        p1HealthBar = new Rectangle(200, 20);
        p1HealthBar.setFill(Color.rgb(40, 40, 40));
        p1HealthBar.setStroke(Color.WHITE);
        p1HealthBar.setStrokeWidth(1);
        p1HealthBar.setX(20);
        p1HealthBar.setY(20);

        p1HealthFill = new Rectangle (196, 16);
        p1HealthFill.setFill(Color.LIME);
        p1HealthFill.setX(22);
        p1HealthFill.setY(22);

        Text p1Label = new Text("P1");
        p1Label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        p1Label.setFill(Color.LIME);
        p1Label.setX(20);
        p1Label.setY(60);

        p1AmmoText = new Text();
        p1AmmoText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        p1AmmoText.setFill(Color.WHITE);
        p1AmmoText.setX(20);
        p1AmmoText.setY(85);

        p1WeaponText = new Text();
        p1WeaponText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        p1WeaponText.setFill(Color.LIGHTGRAY);
        p1WeaponText.setX(20);
        p1WeaponText.setY(105);

        p2HealthBar = new Rectangle(200, 20);
        p2HealthBar.setFill(Color.rgb(40, 40, 40));
        p2HealthBar.setStroke(Color.WHITE);
        p2HealthBar.setStrokeWidth(1);
        p2HealthBar.setX(1060);
        p2HealthBar.setY(20);

        p2HealthFill = new Rectangle(196, 16);
        p2HealthFill.setFill(Color.RED);
        p2HealthFill.setX(1062);
        p2HealthFill.setY(20);

        Text p2Label = new Text("P2");
        p2Label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        p2Label.setFill(Color.RED);
        p2Label.setX(1060);
        p2Label.setY(60);

        p2AmmoText = new Text();
        p2AmmoText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        p2AmmoText.setFill(Color.WHITE);
        p2AmmoText.setX(1060);
        p2AmmoText.setY(85);

        p2WeaponText = new Text();
        p2WeaponText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        p2WeaponText.setFill(Color.LIGHTGRAY);
        p2WeaponText.setX(1060);
        p2WeaponText.setY(105);

        entity = new Entity();
        entity.getViewComponent().addChild(p1HealthBar);
        entity.getViewComponent().addChild(p1HealthFill);
        entity.getViewComponent().addChild(p1Label);
        entity.getViewComponent().addChild(p1AmmoText);
        entity.getViewComponent().addChild(p1WeaponText);
        entity.getViewComponent().addChild(p2HealthBar);
        entity.getViewComponent().addChild(p2HealthFill);
        entity.getViewComponent().addChild(p2Label);
        entity.getViewComponent().addChild(p2AmmoText);
        entity.getViewComponent().addChild(p2WeaponText);

        FXGL.getGameWorld().addEntity(entity);
    }

    public void update(Player player1, Player player2, Weapon weapon1, Weapon weapon2) {
        updatePlayer(player1, weapon1, true);
        updatePlayer(player2, weapon2, false);
    }

    public void update(Player player1, Bot bot, Weapon weapon1, Weapon weapon2) {
        updatePlayer(player1, weapon1, true);
        double healthPercent = bot.getHealth() / (double) bot.getMaxHealth();
        p2HealthFill.setWidth(196 * healthPercent);

        if (healthPercent > 0.5) {
            p2HealthFill.setFill(Color.LIME);
        } else if (healthPercent > 0.25) {
            p2HealthFill.setFill(Color.YELLOW);
        } else {
            p2HealthFill.setFill(Color.RED);
        }

        String p2Ammo = weapon2.getMagazine() + "/" + weapon2.getMaxMagazine();
        String p2Total = weapon2.getCurrentAmmo() + "/" + weapon2.getMaxAmmo();
        String p2Status = weapon2.isReloading() ? " [RELOADING]" : "";
        p2AmmoText.setText(p2Ammo + " " + p2Total + p2Status);
        p2WeaponText.setText(weapon2.getType().getName());
    }

        private void updatePlayer(Player player, Weapon weapon, boolean isPlayer1) {
            double healthPercent = player.getHealth() / (double) player.getMaxHealth();
            Rectangle fill = isPlayer1 ? p1HealthFill : p2HealthFill;
            fill.setWidth(196 * healthPercent);

            if (healthPercent > 0.5) {
                fill.setFill(Color.LIME);
            } else if (healthPercent > 0.25) {
                fill.setFill(Color.YELLOW);
            } else {
                fill.setFill(Color.RED);
            }

            String ammo = weapon.getMagazine() + "/" + weapon.getMaxMagazine();
            String total = weapon.getCurrentAmmo() + "/" + weapon.getMaxAmmo();
            String status = weapon.isReloading() ? " [RELOADING]" : "";

            if (isPlayer1) {
                p1AmmoText.setText(ammo + " " + total + status);
                p1WeaponText.setText(weapon.getType().getName());
            } else {
                p2AmmoText.setText(ammo + " " + total + status);
                p2WeaponText.setText(weapon.getType().getName());
            }



    }
}
