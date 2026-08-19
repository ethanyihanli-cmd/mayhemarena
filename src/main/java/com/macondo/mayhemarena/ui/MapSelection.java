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

import java.util.ArrayList;
import java.util.List;

public class MapSelection {
    private String selectedMap;
    private boolean confirmed;
    private List<Button> mapButtonsList;

    public MapSelection() {
        selectedMap = "Sky Ruins";
        confirmed = false;
        mapButtonsList = new ArrayList<>();
    }

    public String showAndWait() {
        confirmed = false;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #4a4a8a; -fx-border-width: 3;");
        root.setPrefSize(500, 400);

        Label title = new Label("SELECT MAP");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.GOLD);

        Label subTitle = new  Label("Choose your arena");
        subTitle.setFont(Font.font("Arial", 14));
        subTitle.setTextFill(Color.LIGHTGRAY);

        VBox mapButtons = new VBox(15);
        mapButtons.setAlignment(Pos.CENTER);

        String[] maps = {"Sky Ruins", "Split Foundry", "Twin Pits", "Crystal Cavern"};

        for (String map : maps) {
            Button btn = new Button(map);
            btn.setPrefWidth(200);
            btn.setPrefHeight(45);
            applyMapButtonStyle(btn, map);

            btn.setOnAction(e -> {
                selectedMap = map;
                updateMapButtons();
            });

            mapButtonsList.add(btn);
            mapButtons.getChildren().add(btn);
        }

        Button confirmBtn = new Button("SELECT MAP");
        confirmBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #4a7a5a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 30; -fx-background-radius: 8;");
        confirmBtn.setOnAction(e -> {
            confirmed = true;
            stage.close();
        });

        root.getChildren().addAll(title, subTitle, mapButtons, confirmBtn);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        if (confirmed) {
            return selectedMap;
        }
        return null;
    }

    private void updateMapButtons() {
        for (Button button : mapButtonsList) {
            applyMapButtonStyle(button, button.getText());
        }
    }

    private void applyMapButtonStyle(Button button, String map) {
        if (map.equals(selectedMap)) {
            button.setStyle("-fx-background-color: #4a4a8a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8;");
        } else {
            button.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: lightgray; -fx-font-size: 14px; -fx-background-radius: 8;");
        }
    }
}
