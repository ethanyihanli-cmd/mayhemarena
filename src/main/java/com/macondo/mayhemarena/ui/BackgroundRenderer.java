package com.macondo.mayhemarena.ui;

import com.macondo.mayhemarena.model.GameTheme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BackgroundRenderer {
    private static final double WIDTH = 1280;
    private static final double HEIGHT = 720;

    public static void draw(GraphicsContext gc, GameTheme theme) {
        gc.setFill(theme.getBackgroundColor());
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        gc.setStroke(Color.rgb(60, 60, 100, 0.15));
        gc.setLineWidth(1);
        for (int i = 0; i < WIDTH; i += 40) {
            gc.strokeLine(i, 0, i, HEIGHT);
        }
        for (int i = 0; i < HEIGHT; i += 40) {
            gc.strokeLine(0, i, WIDTH, i);
        }

        gc.setFill(Color.rgb(30, 30, 80, 0.1));
        gc.fillRect(0, HEIGHT - 100, WIDTH, 100);

        gc.setFill(Color.rgb(100, 100, 200, 0.05));
        gc.fillRect(0, 0, 200, 200);
        gc.fillRect(WIDTH - 200, 0, 200, 200);
        gc.fillRect(0, HEIGHT - 200, 200, 200);
        gc.fillRect(WIDTH - 200, HEIGHT - 200, 200, 200);


    }
}
