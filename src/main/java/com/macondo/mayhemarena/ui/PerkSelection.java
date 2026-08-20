package com.macondo.mayhemarena.ui;

import com.macondo.mayhemarena.model.PerkType;
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

public class PerkSelection {
    private PerkType p1Perk;
    private PerkType p2Perk;
    private boolean confirmed;

    public PerkSelection() {
        p1Perk = null;
        p2Perk = null;
        confirmed = false;
    }

    public PerkType[] showAndWait() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #4a4a8a; -fx-border-width: 3;");
        root.setPrefSize(600, 450);

        Label title = new Label("SELECT PERKS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.GOLD);

        Label sub = new Label("Choose a perk for each player");
        sub.setFont(Font.font("Arial", 14));
        sub.setTextFill(Color.LIGHTGRAY);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        Label p1Label = new Label("Player 1 (Green)");
        p1Label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        p1Label.setTextFill(Color.LIME);
        grid.add(p1Label, 0, 0);

        VBox p1Box = new VBox(10);
        for (PerkType perk : PerkType.values()) {
            Button btn = new Button(perk.getName());
            btn.setPrefWidth(140);
            btn.setWrapText(true);
            if (perk == p1Perk) {
                btn.setStyle("-fx-background-color: #2a5a2a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
            } else {
                btn.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: lightgray; -fx-font-size: 12px;");
            }
            btn.setOnAction(e -> {
                p1Perk = perk;
                stage.close();
                PerkType[] result = new PerkSelection.showAndWait();
                if (result != null) {
                    p1Perk = result[0];
                    p2Perk = result[1];
                }
            });
            p1Box.getChildren().add(btn);
        }
        grid.add(p1Box, 0, 1);

        Label p2Label = new Label("Player 2 (Red)");
        p2Label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        p2Label.setTextFill(Color.RED);
        grid.add(p2Label, 1, 0);

        VBox p2Box = new VBox(10);
        for (PerkType perk : PerkType.values()) {
            Button btn = new Button(perk.getName());
            btn.setPrefWidth(140);
            btn.setWrapText(true);
            if (perk == p2Perk) {
                btn.setStyle("-fx-background-color: #5a2a2a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
            } else {
                btn.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: ;ightgray; -fx-font-size: 12px;");
            }
            btn.setOnAction(e -> {
                p2Perk = perk;
                stage.close();
                PerkType[] result = new PerkSelection().showAndWait();
                if (result != null) {
                    p1Perk = result[0];
                    p2Perk = result[1];
                }
            });
            p2Box.getChildren().add(btn);
        }
        grid.add(p2Box, 1, 1);

        Button confirm = new Button("START MATCH");
        confirm.setStyle("-fx-font-size: 16px; -fx-background-color: #4a7a5a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 30; -fx-background-radius: 8;");
        confirm.setOnAction(e -> {
            confirmed = true;
            stage.close();
        });

        root.getChildren().addAll(title, sub, grid, confirm);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        if (confirmed) {
            return new PerkType[]{p1Perk, p2Perk};
        }
        return null;
    }

}
