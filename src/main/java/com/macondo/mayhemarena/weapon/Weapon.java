package com.macondo.mayhemarena.weapon;

import com.macondo.mayhemarena.entity.Bullet;
import com.macondo.mayhemarena.util.SoundManager;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private WeaponType type;
    private int currentAmmo;
    private int magazine;
    private boolean isReloading;
    private double reloadTimer;
    private double shootCooldown;

    public Weapon(WeaponType type) {
        this.type = type;
        this.currentAmmo = type.getMaxAmmo();
        this.magazine = type.getMagazineSize();
        this.isReloading = false;
        this.reloadTimer = 0;
        this.shootCooldown = 0;
    }

    public List<Bullet> shoot(double x, double y, int facing, int shooterId) {
        List<Bullet> bullets = new ArrayList<>();

        if (isReloading || shootCooldown > 0) {
            return bullets;
        }

        if (type == WeaponType.KNIFE) {
            bullets.add(new Bullet(x, y, facing, type.getDamage(), type.getKnockback(), type.getRange(), shooterId,
                    0, type.getBulletSpeed(), type.getBulletWidth(), type.getBulletHeight(),
                    type.getBulletLife(), Color.web("#fca5a5")));
            shootCooldown = type.getCooldown();
            SoundManager.getInstance().playShoot();
            return bullets;
        }

        if (magazine <= 0) {
            return bullets;
        }

        int pelletCount = (type == WeaponType.SHOTGUN) ? 5 : 1;

        for (int i = 0; i < pelletCount; i++) {
            double spread = (type == WeaponType.SHOTGUN)
                    ? (i - (pelletCount - 1) / 2.0) * 24
                    : 0;

            if (type == WeaponType.SNIPER) {
                spread += (Math.random() - 0.5) * 8;
            }

            int adjustedKnockback = type.getKnockback();

            if (type == WeaponType.SHOTGUN) {
                adjustedKnockback = (int) (type.getKnockback() * 0.5);
            }

            Bullet bullet = new Bullet(x, y, facing, type.getDamage(),
                    adjustedKnockback, type.getRange(), shooterId, spread,
                    type.getBulletSpeed(), type.getBulletWidth(), type.getBulletHeight(),
                    type.getBulletLife(), bulletColor());
            bullets.add(bullet);
        }

        magazine--;
        shootCooldown = type.getCooldown();
        SoundManager.getInstance().playShoot();

        return bullets;
    }

    public void update(double delta) {
        if (shootCooldown > 0) {
            shootCooldown -= delta;
        }

        if (isReloading) {
            reloadTimer -= delta;
            if (reloadTimer <= 0) {
                isReloading = false;
                if (type != WeaponType.KNIFE) {
                    int needed = type.getMagazineSize() - magazine;
                    int available = Math.min(needed, currentAmmo);
                    magazine += available;
                    currentAmmo -= available;
                } else {
                    magazine = type.getMagazineSize();
                }
            }
        }
    }

    public void reload() {
        if (type == WeaponType.KNIFE) {
            return;
        }
        if (isReloading || magazine == type.getMagazineSize() || currentAmmo <= 0) {
            return;
        }
        isReloading = true;
        reloadTimer = type.getReloadTime();
    }

    public boolean canShoot() {
        if (type == WeaponType.KNIFE) {
            return !isReloading && shootCooldown <= 0;
        }
        return !isReloading && magazine > 0 && shootCooldown <= 0;
    }

    public int getRecoil() {
        return type.getRecoil();
    }

    public WeaponType getType() { return type; }
    public int getMagazine() { return magazine; }
    public int getMaxMagazine() { return type.getMagazineSize(); }
    public int getCurrentAmmo() { return currentAmmo; }
    public int getMaxAmmo() { return type.getMaxAmmo(); }
    public boolean isReloading() { return isReloading; }
    public double getReloadProgress() {
        if (!isReloading) return 1.0;
        return 1.0 - (reloadTimer / type.getReloadTime());
    }

    private Color bulletColor() {
        return switch (type) {
            case PISTOL -> Color.web("#fadb5f");
            case RIFLE -> Color.web("#feca57");
            case SHOTGUN -> Color.web("#ff9f43");
            case SNIPER -> Color.web("#bfdbfe");
            case KNIFE -> Color.web("#fca5a5");
        };
    }

}
