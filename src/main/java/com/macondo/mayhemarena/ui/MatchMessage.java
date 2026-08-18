package com.macondo.mayhemarena.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class MatchMessage {
    private Label label;
    private StackPane container;

    public MatchMessage() {
        label = new Label();
        label.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        label.setTextFill(Color.WHITE);

        container = new StackPane(label);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(1280, 720);
        container.setMouseTransparent(true);
        container.setVisible(false);
    }

    public void show(String text, Color color) {
        label.setText(text);
        label.setTextFill(color);
        container.setVisible(true);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), label);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.play();

        FadeTransition fade = new FadeTransition(Duration.millis(300), container);
        fade.setDelay(Duration.millis(2000));
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            container.setVisible(false);
            container.setOpacity(1.0);
        });
        fade.play();

    }

    public StackPane getContainer() {
        return container;
    }



}
