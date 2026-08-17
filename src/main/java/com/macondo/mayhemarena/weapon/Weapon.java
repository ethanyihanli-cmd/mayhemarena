package com.macondo.mayhemarena.weapon;

import com.macondo.mayhemarena.entity.Bullet;

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

        if (isReloading || magazine <= 0 || shootCooldown > 0) {
            return bullets;
        }

        if (type == WeaponType.KNIFE) {
            magazine--;
            return bullets;
        }

        int pelletCount = (type == WeaponType.SHOTGUN) ? 5 : 1;

        for (int i = 0; i < pelletCount; i++) {
            double spread = (type == WeaponType.SHOTGUN) {
                ? (i - pelletCount / 2.0) * 0.15
                        : 0;

                if (type == WeaponType.SNIPER) {
                    spread += (Math.random() - 0.5) * 0.05;
                }

                int adjustedKnockback = type.getKnockback();

                if (type == WeaponType.SHOTGUN) {
                    adjustedKnockback = (int)(type.getKnockback() * 0.5);
                }

                Bullet bullet = new Bullet(x, y, facing, type.getDamage(),
                        adjustedKnockback, type.getRange(), shooterId);
                bullets.add(bullet);
            }

            magazine--;
            shootCooldown = (type == WeaponType.SHOTGUN) ? 0.5 :
                    (type == WeaponType.RIFLE) ? 0.12 :
                    (type == WeaponType.SNIPER) ? 1.0 :
                    (type == WeaponType.PISTOL) ? 0.35 : 0;

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
                    int needed = type.getMagazineSize() - magazine;
                    int available = Math.min(needed, currentAmmo);
                    magazine += available;
                    currentAmmo -= available;
                }
            }
        }

        public void reload() {
            if (isReloading || magazine == type.getMagazineSize() || currentAmmo <= 0) {
                return;
            }
            isReloading = true;
            reloadTimer = type.getReloadTime();
        }

        public boolean canShoot() {
            return !isReloading && magazine > 0 && shootCooldown <= 0;
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

    }

}
