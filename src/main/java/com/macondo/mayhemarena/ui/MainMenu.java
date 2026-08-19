package com.macondo.mayhemarena.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenu {
    private boolean startGame;
    private boolean exitGame;

    public MainMenu() {
        startGame = false;
        exitGame = false;
    }

    public boolean showAndWait() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #4a4a8a; -fx-border-width: 3");
        root.setPrefSize(500, 400);

        Label title = new Label("MAYHEM ARENA");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.GOLD);

        Label subtitle = new Label("Local Multiplayer Shooter");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setTextFill(Color.LIGHTGRAY);

        Label controlsLabel = new Label(
                "P1: WASD | SPACE Shoot | R Reload\n" +
                        "P2: Arrows | M Shoot | , Reload"
        );
        controlsLabel.setFont(Font.font("Arial", 14));
        controlsLabel.setTextFill(Color.WHITE);
        controlsLabel.setStyle("-fx-text-alignment: center; -fx-padding: 20;");

        Button startBtn = new Button("START GAME");
        startBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #4a7a5a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 10 40; -fx-background-radius: 8;");
        startBtn.setOnAction(e -> {
            startGame = true;
            stage.close();
        });

        Button exitBtn = new Button("EXIT");
        exitBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #5a2a2a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 30; -fx-background-radius: 8;");
        exitBtn.setOnAction(e -> {
            exitGame = true;
            stage.close();
        });

        root.getChildren().addAll(title, subtitle, controlsLabel, startBtn, exitBtn);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        return startGame;

    }

}
