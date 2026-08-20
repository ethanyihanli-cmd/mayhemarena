package com.macondo.mayhemarena.model;

public enum PerkType {
    DOUBLE_JUMP("Double Jump", "Allows a second jump in air"),
    SPEED_BOOST("Speed Boost", "Increases movement speed by 20%"),
    KNOCKBACK_RESIST("Knockback Resist", "Reduces knockback taken by 40%"),
    HEALTH_BOOST("Health Boost", "Increases max health by 30");

    private String name;
    private String description;

    PerkType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }


}
