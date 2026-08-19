package com.macondo.mayhemarena.model;

import javafx.scene.paint.Color;

public class GameTheme {
    private String name;
    private Color backgroundColor;
    private Color platformColor;
    private Color player1Color;
    private Color player2Color;
    private Color bulletColor;
    private Color hudColor;

    public GameTheme(String name, Color backgroundColor, Color platformColor
                     Color player1Color, Color player2Color, Color bulletColor, Color hudColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.platformColor = platformColor;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        this.bulletColor = bulletColor;
        this.hudColor = hudColor;
    }

    public String getName() { return name; }
    public Color getBackgroundColor() { return backgroundColor; }
    public Color getPlatformColor() { return platformColor; }
    public Color getPlayer1Color() { return player1Color; }
    public Color getPlayer2Color() { return player2Color; }
    public Color getBulletColor() { return bulletColor; }
    public Color getHudColor() { return hudColor; }

    public static GameTheme defaultTheme() {
        return new GameTheme(
                "Default",
                Color.rgb(20, 20, 40),
                Color.GRAY,
                Color.GREEN,
                Color.RED,
                Color.YELLOW,
                Color.WHITE
        );

    }


}
