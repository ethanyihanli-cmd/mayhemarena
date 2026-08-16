package com.macondo.mayhemarena.weapon;

public enum WeaponType {
    PISTOL("Pistol", 13, 13, 15, 2, 350, 0.35, 0),
    RIFLE("Rifle", 30, 30, 8, 4, 550, 0.12, 2),
    SHOTGUN("Shotgun", 10, 10, 12, 6, 250, 0.5, 4),
    SNIPER("Sniper", 7, 7, 30, 8, 600, 1, 6),
    KNIFE("Knife", Integer.MAX_VALUE, 0, 5, 1, 0, 0.5, 0);

    private String name;
    private int maxAmmo;
    private int magazineSize;
    private int damage;
    private int knockback;
    private int range;
    private double reloadTime;
    private int recoil;

    WeaponType(String name, int maxAmmo, int magazineSize, int damage,
               int knockback, int range, double reloadTime, int recoil) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.magazineSize = magazineSize;
        this.damage = damage;
        this.knockback = knockback;
        this.range = range;
        this.reloadTime = reloadTime;
        this.recoil = recoil;

    }

    public String getName() { return name; }
    public int getMaxAmmo() { return maxAmmo; }
    public int getMagazineSize() { return magazineSize; }
    public int getDamage() { return damage; }
    public int getKnockback() {return knockback; }
    public int getRange() { return range; }
    public int getReloadTime() { return reloadTime; }
    public int getRecoil() { return recoil; }
}