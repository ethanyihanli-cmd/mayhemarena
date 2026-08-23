package com.macondo.mayhemarena.config;

import javafx.scene.input.KeyCode;

public class ControlScheme {
     private KeyCode leftKey;
     private KeyCode rightKey;
     private KeyCode jumpKey;
     private KeyCode downKey;
     private KeyCode shootKey;

     private KeyCode reloadKey;

     public ControlScheme(KeyCode left, KeyCode right, KeyCode jump, KeyCode down, KeyCode shoot) {
          this.leftKey = left;
          this.rightKey = right;
          this.jumpKey = jump;
          this.downKey = down;
          this.shootKey = shoot;
          this.reloadKey = KeyCode.R;
     }

     public ControlScheme(KeyCode left, KeyCode right, KeyCode jump, KeyCode down, KeyCode shoot, KeyCode reload) {
         this.leftKey = left;
         this.rightKey = right;
         this.jumpKey = jump;
         this.downKey = down;
         this.shootKey = shoot;
         this.reloadKey = reload;
     }

     public static ControlScheme playerOneDefault() {
          return new ControlScheme(KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE);
     }

     public static ControlScheme playerTwoDefault() {
         return new ControlScheme(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.DOWN, KeyCode.M, KeyCode.COMMA);
     }

     public KeyCode getKey(PlayerAction action) {
         switch (action) {
             case LEFT:
                 return leftKey;
             case RIGHT:
                 return rightKey;
             case JUMP:
                 return jumpKey;
             case DOWN:
                 return downKey;
             case SHOOT:
                 return shootKey;
             default:
                 return null;
          }
     }

     public KeyCode getReloadKey() {
         return reloadKey;
     }
}
