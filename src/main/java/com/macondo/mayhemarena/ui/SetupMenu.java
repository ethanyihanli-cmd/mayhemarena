package com.macondo.mayhemarena.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SetupMenu {;
    private String selectedMap;
    private boolean vsBot;
    private boolean confirmed;

    public SetupMenu() {
        selectedMap = "Sky Ruins";
        vsBot = true;
        confirmed = false;
    }

    public SetupResult showAndWait() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #4a4a8a; -fx-border-width: 3;");
        root.setPrefSize(500, 450);

        Label title = new Label("MAYHEM ARENA");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.GOLD);

        Label mapLabel = new Label("Selected Map");
        mapLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        mapLabel.setTextFill(Color.WHITE);

        ToggleGroup mapGroup = new ToggleGroup();
        String[] maps = {"Sky Ruins", "Split Foundry", "Twin Pits", "Crystal Cavern"};
        VBox mapBox = new VBox(10);
        for (String map : maps) {
            RadioButton rb = new RadioButton(map);
            rb.setToggleGroup(mapGroup);
            rb.setTextFill(Color.LIGHTGRAY);
            rb.setStyle("-fx-background-color: transparent;");
            if (map.equals(selectedMap)) rb.setSelected(true);
            rb.setOnAction(e -> selectedMap = map);
            mapBox.getChildren().add(rb);
        }
        mapBox.setAlignment(Pos.CENTER_LEFT);

        Label modeLabel = new Label("Game Mode");
        modeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        modeLabel.setTextFill(Color.WHITE);

        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton vsBotRb = new RadioButton("VS Bot");
        vsBotRb.setToggleGroup(modeGroup);
        vsBotRb.setTextFill(Color.LIGHTGRAY);
        vsBotRb.setStyle("-fx-background-color: transparent;");
        vsBotRb.setSelected(true);

        RadioButton vsPlayerRb = new RadioButton("VS Player");
        vsPlayerRb.setToggleGroup(modeGroup);
        vsPlayerRb.setTextFill(Color.LIGHTGRAY);
        vsPlayerRb.setStyle("-fx-background-color: transparent;");

        VBox modeBox = new VBox(10, vsBotRb, vsPlayerRb);
        modeBox.setAlignment(Pos.CENTER_LEFT);

        vsBotRb.setOnAction(e -> vsBot = true);
        vsPlayerRb.setOnAction(e -> vsBot = false);

        Button nextBtn = new Button("NEXT ->");
        nextBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #4a4a8a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 30; -fx-background-radius: 8;");
        nextBtn.setOnAction(e -> {
            confirmed = true;
            stage.close();
        });

        root.getChildren().addAll(title, mapLabel, mapBox, modeLabel, modeBox, nextBtn);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        if (confirmed) {
            return new SetupResult(selectedMap, vsBot);
        }
        return null;
    }

    public static class SetupResult {
        public String map;
        public boolean vsBot;

        public SetupResult(String map, boolean vsBot) {
            this.map = map;
            this.vsBot = vsBot;
        }
    }
}
