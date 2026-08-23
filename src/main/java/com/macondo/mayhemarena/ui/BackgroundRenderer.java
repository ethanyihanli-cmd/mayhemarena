package com.macondo.mayhemarena.ui;

import com.macondo.mayhemarena.model.GameTheme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BackgroundRenderer {
    private static final double WIDTH = 1280;
    private static final double HEIGHT = 720;

    public static void draw(GraphicsContext gc, GameTheme theme) {
        Color top = Color.rgb(10, 15, 40);
        Color mid = Color.rgb(25, 30, 70);
        Color bottom = Color.rgb(40, 35, 80);

        for (int i = 0; i < HEIGHT; i++) {
            double progress = (double) i / HEIGHT;
            Color color;
            if (progress < 0.5) {
                double t = progress / 0.5;
                color = top.interpolate(mid, t);
            } else {
                double t = (progress - 0.5) / 0.5;
                color = mid.interpolate(bottom, t);
            }
            gc.setFill(color);
            gc.fillRect(0, i, WIDTH, 1);
        }
        gc.setGlobalAlpha(0.15);
        gc.setFill(Color.rgb(255, 200, 100));
        gc.fillOval(WIDTH - 200, 40, 300, 250);
        gc.setFill(Color.rgb(255, 150, 50));
        gc.fillOval(WIDTH - 150, 80, 200, 180);
        gc.setGlobalAlpha(1);

        gc.setFill(Color.rgb(255, 220, 120));
        gc.fillOval(WIDTH - 120, 70, 80, 80);

        gc.setGlobalAlpha(0.3);
        gc.setStroke(Color.rgb(255, 200, 100));
        gc.setLineWidth(3);
        gc.strokeOval(WIDTH - 120, 70, 80, 80);
        gc.setGlobalAlpha(1);

        gc.setFill(Color.rgb(20, 25, 50, 0.7));
        double[] farMountainX = {0, 150, 300, 450, 600, 750, 900, 1050, 1280};
        double[] farMountainY = {500, 350, 430, 280, 380, 320, 450, 360, 500};
        gc.fillPolygon(farMountainX, farMountainY, farMountainX.length);

        gc.setFill(Color.rgb(30, 35, 65, 0.6));
        double[] midMountainX = {0, 200, 400, 550, 750, 950, 1150, 1280};
        double[] midMountainY = {520, 380, 460, 350, 420, 380, 470, 520};
        gc.fillPolygon(midMountainX, midMountainY, midMountainX.length);

        gc.setFill(Color.rgb(40, 45, 80, 0.5));
        double[] nearMountainX = {0, 250, 500, 650, 850, 1050, 1280};
        double[] nearMountainY = {540, 420, 490, 400, 460, 430, 540};
        gc.fillPolygon(nearMountainX, nearMountainY, nearMountainX.length);

        gc.setFill(Color.rgb(30, 35, 60));
        gc.fillRect(0, 540, WIDTH, 180);

        gc.setStroke(Color.rgb(50, 55, 80, 0.3));
        gc.setLineWidth(1);
        for (int i = 0; i < WIDTH; i += 30) {
            gc.strokeLine(i, 540, i + 20, 545);
        }

        gc.setFill(Color.rgb(255, 255, 255, 0.3));
        int[] starX = {50, 120, 200, 350, 500, 680, 800, 950, 1100, 1200};
        int[] starY = {30, 80, 50, 20, 100, 40, 70, 25, 60, 90};
        for (int i = 0; i < starX.length; i++) {
            gc.fillOval(starX[i], starY[i], 2, 2);
        }

        gc.setFill(Color.rgb(200, 200, 255, 0.05));
        for (int i = 0; i < 30l; i++){
            double px = (i * 47 + 13) % WIDTH;
            double py = (i * 31 + 7) % HEIGHT;
            gc.fillOval(px, py, 3, 3);
        }


    }



}
