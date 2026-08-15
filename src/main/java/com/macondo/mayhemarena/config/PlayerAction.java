package com.macondo.mayhemarena.config;

public enum PlayerAction {
    LEFT("Left"),
    RIGHT("Right"),
    JUMP("Jump"),
    DOWN("Down"),
    SHOOT("Shoot");

    private final String label;

    PlayerAction(String label) {
        this.label = label;
    }
     public String getLabel() {
        return label;
     }
}
