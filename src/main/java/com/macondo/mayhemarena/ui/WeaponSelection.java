package com.macondo.mayhemarena.ui;

import com.macondo.mayhemarena.weapon.WeaponType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class WeaponSelection {
    private WeaponType selectedPlayer1;
    private WeaponType selectedPlayer2;
    private boolean confirmed;

    public WeaponSelection() {
        selectedPlayer1 = WeaponType.PISTOL;
        selectedPlayer2 = WeaponType.PISTOL;
        confirmed = false;
    }

    public WeaponType[] showAndWait() {
        confirmed = false;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setFont("-fx-background-color: #1a1a2e; -fx-border-color: #4a4a8a; -fx-border-width: 3;");
        root.setPrefSize(600, 450);

        Label title = new Label("SELECTED WEAPONS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.GOLD);

        Label subTitle = new Label("Choose weapons for both players");
        subTitle.setFont(Font.font("Arial", 14));
        subTitle.setTextFill(Color.LIGHTGRAY);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        Label p1Label = new Label("Player 1 (green)");
        p1Label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        p1Label.setTextFill(Color.LIME);
        grid.add(p1Label, 0, 0);

        VBox p2Buttons = new VBox(10);
        for (WeaponType type : WeaponType.values()) {
            Button btn = createWeaponButton(type, 2);
            p2Buttons.getChildren().add(btn);
        }
        grid.add(p2Buttons, 1, 1);

        Button confirmBtn = new Button("START MATCH");
        confirmBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #4a7a5a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 8;");
        confirmBtn.setOnAction(e -> {
            confirmed = true;
            stage.close();
        });

        root.getChildren().addAll(title, subTitle, grid, confirmBtn);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        if (confirmed) {
            return new WeaponType[]{selectedPlayer1, selectedPlayer2};
        }
        return null;
    }

    private Button createWeaponButton(WeaponType type, int player) {
        Button btn = new Button(type.getName());
        btn.setPrefWidth(120);

        if (player == 1) {
            if (type == selectedPlayer1) {
                btn.setStyle("-fx-background-color: #2a5a2a; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                btn.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: lightgray;");
            }
            btn.setOnAction(e -> {
                selectedPlayer1 = type;
                updateButtons();
            });
        } else {
            if (type == selectedPlayer2) {
                btn.setStyle("-fx-background-color: #5a2a2a; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                btn.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: lightgray;");
            }
            btn.setOnAction(e -> {
                selectedPlayer2 = type;
                updateButtons();
            });
        }

        return btn;
    }

    private void updateButtons() {

    }

}
