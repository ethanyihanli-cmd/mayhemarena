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
    private Label subLabel;
    private StackPane container;

    public MatchMessage() {
        label = new Label();
        label.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        label.setTextFill(Color.WHITE);

        subLabel = new Label();
        subLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        subLabel.setTextFill(Color.WHITE);

        container = new StackPane(label, subLabel);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(1280, 720);
        container.setMouseTransparent(true);
        container.setVisible(false);
        StackPane.setAlignment(label, Pos.CENTER);
        StackPane.setAlignment(subLabel, Pos.BOTTOM_CENTER);
        subLabel.setTranslateY(60);
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

    public void showMatchResult(String text, String subText, Color color) {
        label.setText(text);
        label.setTextFill(color);
        subLabel.setText(subText);
        subLabel.setTextFill(Color.WHITE);
        container.setVisible(true);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), label);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.play();

        ScaleTransition subScale = new ScaleTransition(Duration.millis(400), subLabel);
        subScale.setFromX(0.5);
        subScale.setFromY(0.5);
        subScale.setToX(1.0);
        subScale.setToY(1.0);
        subScale.play();
    }

    public void hide() {
        container.setVisible(false);
        container.setOpacity(1.0);
        label.setText("");
        subLabel.setText("");
    }

    public StackPane getContainer() {
        return container;
    }

    public boolean isVisible() {
        return container.isVisible();
    }
}
