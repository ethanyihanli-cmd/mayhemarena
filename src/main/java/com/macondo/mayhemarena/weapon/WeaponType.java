package com.macondo.mayhemarena.weapon;

public enum WeaponType {
    PISTOL("Pistol", 48, 12, 22, 180, 1.6, 0.28, 1.1, 40, 950, 12, 3),
    RIFLE("Rifle", 90, 30, 16, 110, 1.6, 0.12, 1.5, 22, 1100, 18, 3),
    SHOTGUN("Shotgun", 18, 6, 15, 120, 0.45, 0.88, 1.8, 110, 820, 8, 8),
    SNIPER("Sniper", 21, 7, 42, 760, 1.8, 1.25, 2.1, 160, 1320, 8, 3),
    KNIFE("Knife", Integer.MAX_VALUE, 0, 24, 360, 0.1, 0.42, 0, 60, 0, 42, 18);

    private String name;
    private int maxAmmo;
    private int magazineSize;
    private int damage;
    private int knockback;
    private double bulletLife;
    private double cooldown;
    private double reloadTime;
    private int recoil;
    private double bulletSpeed;
    private double bulletWidth;
    private double bulletHeight;

    WeaponType(String name, int maxAmmo, int magazineSize, int damage,
               int knockback, double bulletLife, double cooldown, double reloadTime, int recoil,
               double bulletSpeed, double bulletWidth, double bulletHeight) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.magazineSize = magazineSize;
        this.damage = damage;
        this.knockback = knockback;
        this.bulletLife = bulletLife;
        this.cooldown = cooldown;
        this.reloadTime = reloadTime;
        this.recoil = recoil;
        this.bulletSpeed = bulletSpeed;
        this.bulletWidth = bulletWidth;
        this.bulletHeight = bulletHeight;

    }

    public String getName() { return name; }
    public int getMaxAmmo() { return maxAmmo; }
    public int getMagazineSize() { return magazineSize; }
    public int getDamage() { return damage; }
    public int getKnockback() {return knockback; }
    public int getRange() { return (int) (bulletSpeed * bulletLife); }
    public double getBulletLife() { return bulletLife; }
    public double getCooldown() { return cooldown; }
    public double getReloadTime() { return reloadTime; }
    public int getRecoil() { return recoil; }
    public double getBulletSpeed() { return bulletSpeed; }
    public double getBulletWidth() { return bulletWidth; }
    public double getBulletHeight() { return bulletHeight; }
}
