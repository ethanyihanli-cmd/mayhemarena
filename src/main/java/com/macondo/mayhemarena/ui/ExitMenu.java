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

public class ExitMenu {
    public enum ExitAction {
        RESUME,
        MENU,
        EXIT
    }

    public ExitAction showAndWait() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: rgba(20, 20, 40, 0.95); -fx-border-color: #4a4a8a; -fx-border-width: 3;");
        root.setPrefSize(350, 250);

        Label title = new Label("PAUSED");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.GOLD);

        Label sub = new Label("What would you like to do?");
        sub.setFont(Font.font("Arial", 14));
        sub.setTextFill(Color.LIGHTGRAY);

        Button resumeBtn = new Button("Resume");
        resumeBtn.setPrefWidth(200);
        resumeBtn.setPrefHeight(40);
        resumeBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #4a7a5a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8;");
        resumeBtn.setOnAction(e -> {
            stage.close();
        });

        Button menuBtn = new Button("Main Menu");
        menuBtn.setPrefWidth(200);
        menuBtn.setPrefHeight(40);
        menuBtn.setStyle("-fx-font-size: 14px; -fx-background-colors: #4a4a8a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8;");
        menuBtn.setOnAction(e -> {
            stage.close();
        });

        Button exitBtn = new Button("Exit Game");
        exitBtn.setPrefWidth(200);
        exitBtn.setPrefHeight(40);
        exitBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #8a2a2a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8;");
        exitBtn.setOnAction(e -> {
            stage.close();
        });

        final ExitAction[] result = {ExitAction.RESUME};

        resumeBtn.setOnAction(e -> {
            result[0] = ExitAction.RESUME;
            stage.close();
        });

        menuBtn.setOnAction(e -> {
            result[0] = ExitAction.MENU;
            stage.close();
        });

        exitBtn.setOnAction(e -> {
            result[0] = ExitAction.EXIT;
            stage.close();
        });

        root.getChildren().addAll(title, sub, resumeBtn, menuBtn, exitBtn);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        return result[0];


    }

}
