package com.macondo.mayhemarena.ui;

import com.macondo.mayhemarena.config.ControlScheme;
import com.macondo.mayhemarena.config.PlayerAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


public class ControlRemap {
    private ControlScheme p1Controls;
    private ControlScheme p2Controls;
    private boolean confirmed;

    private enum CaptureState {
        NONE,
        P1_LEFT, P1_RIGHT, P1_JUMP, P1_DOWN, P1_SHOOT,
        P2_LEFT, P2_RIGHT, P2_JUMP, P2_DOWN, P2_SHOOT
    }
    private CaptureState captureState;

    public ControlRemap() {
        p1Controls = ControlScheme.playerOneDefault();
        p2Controls = ControlScheme.playerTwoDefault();
        confirmed = false;
        captureState = CaptureState.NONE;
    }

    public ControlScheme[] showAndWait() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #4a4a8a; -fx-border-width: 3;");
        root.setPrefSize(700,  500);

        Label title = new Label("CONTROL REMAPPING");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.GOLD);

        Label sub = new Label("Click a key to change it, then press the new key");
        sub.setFont(Font.font("Arial", 14));
        sub.setTextFill(Color.LIGHTGRAY);

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        Label p1Header = new Label("Player 1 (Green)");
        p1Header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        p1Header.setTextFill(Color.LIME);
        grid.add(p1Header, 0, 0);

        Label p2Header = new Label("Player 2 (Red)");
        p2Header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        p2Header.setTextFill(Color.RED);
        grid.add(p2Header, 1, 0);

        String[] actions = {"Left", "Right", "Jump", "Down", "Shoot"};
        PlayerAction[] actionEnums = {PlayerAction.LEFT, PlayerAction.RIGHT, PlayerAction.JUMP, PlayerAction.DOWN, PlayerAction.SHOOT};

        for (int i = 0; i < actions.length; i++) {
            Label actionLabel = new Label(actions[i]);
            actionLabel.setFont(Font.font("Arial", 14));
            actionLabel.setTextFill(Color.WHITE);
            grid.add(actionLabel, 0, i + 1);

            Button p1Btn = createKeyButton(p1Controls.getKey(actionEnums[i]), actionEnums[i], 1);
            grid.add(p1Btn, 0, i + 1);

            Button p2Btn = createKeyButton(p2Controls.getKey(actionEnums[i]), actionEnums[i], 2);
            grid.add(p2Btn, 1, i + 1);
        }

        Button confirmBtn = new Button("SAVE & CONTINUE");
        confirmBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #4a7a5a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 30; -fx-background-radius: 8;");
        confirmBtn.setOnAction(e -> {
            confirmed = true;
            stage.close();
        });

        Button resetBtn = new Button("RESET TO DEFAULT");
        resetBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #4a4a8a; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 6 20; -fx-background-radius: 8;");
        resetBtn.setOnAction(e -> {
            p1Controls = ControlScheme.playerOneDefault();
            p2Controls = ControlScheme.playerTwoDefault();
            stage.close();
            ControlRemap fresh = new ControlRemap();
            ControlScheme[] result = fresh.showAndWait();
            if (result != null) {
                p1Controls = result[0];
                p2Controls = result[1];
            }
        });

        VBox buttonBox = new VBox(10, confirmBtn, resetBtn);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, sub, grid, buttonBox);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
            if (captureState != CaptureState.NONE) {
                KeyCode newKey = e.getCode();
                if (newKey == KeyCode.ESCAPE) {
                    captureState = CaptureState.NONE;
                    return;
                }

                applyKey(newKey);
                captureState = CaptureState.NONE;
                stage.close();
                ControlRemap fresh = new ControlRemap();
                fresh.p1Controls = this.p1Controls;
                fresh.p2Controls = this.p2Controls;
                ControlScheme[] result = fresh.showAndWait();
                if (result != null) {
                    this.p1Controls = result[0];
                    this.p2Controls = result[1];
                } else {

                }
                e.consume();
            }
        });

        stage.showAndWait();

        if (confirmed) {
            return new ControlScheme[]{p1Controls, p2Controls};
        }
        return null;
    }

    private Button createKeyButton(KeyCode key, PlayerAction action, int player) {
        Button btn = new Button(key.getName());
        btn.setPrefWidth(80);
        btn.setStyle("-fx-background-color: #2a2a4a; -fx-text-fill: white; -fx-font-weight: bold;");

        btn.setOnAction(e -> {
            if (player == 1) {
                captureState = CaptureState.values()[action.ordinal() + 1];
            } else {
                captureState = CaptureState.values()[action.ordinal() + 6];
            }
            btn.setText("Press key...");
            btn.setStyle("-fx-background-color: #4a4a8a; -fx-text-fill: yellow; -fx-font-weight: bold;");
        });

        return btn;
    }

    private void applyKey(KeyCode newKey) {
        switch (captureState) {
            case P1_LEFT -> p1Controls = new ControlScheme(newKey, p1Controls.getKey(PlayerAction.RIGHT),
                    p1Controls.getKey(PlayerAction.JUMP), p1Controls.getKey(PlayerAction.DOWN),
                    p1Controls.getKey(PlayerAction.SHOOT));
            case P1_RIGHT -> p1Controls = new ControlScheme(p1Controls.getKey(PlayerAction.LEFT), newKey,
                    p1Controls.getKey(PlayerAction.JUMP), p1Controls.getKey(PlayerAction.DOWN),
                    p1Controls.getKey(PlayerAction.SHOOT));
            case P1_JUMP -> p1Controls = new ControlScheme(p1Controls.getKey(PlayerAction.LEFT),
                    p1Controls.getKey(PlayerAction.RIGHT), newKey, p1Controls.getKey(PlayerAction.DOWN),
                    p1Controls.getKey(PlayerAction.SHOOT));
            case P1_DOWN -> p1Controls = new ControlScheme(p1Controls.getKey(PlayerAction.LEFT),
                    p1Controls.getKey(PlayerAction.RIGHT), p1Controls.getKey(PlayerAction.JUMP), newKey,
                    p1Controls.getKey(PlayerAction.SHOOT));
            case P1_SHOOT -> p1Controls = new ControlScheme(p1Controls.getKey(PlayerAction.LEFT),
                    p1Controls.getKey(PlayerAction.RIGHT), p1Controls.getKey(PlayerAction.JUMP),
                    p1Controls.getKey(PlayerAction.DOWN), newKey);
            case P2_LEFT -> p2Controls = new ControlScheme(newKey, p2Controls.getKey(PlayerAction.RIGHT),
                    p2Controls.getKey(PlayerAction.JUMP), p2Controls.getKey(PlayerAction.DOWN),
                    p2Controls.getKey(PlayerAction.SHOOT));
            case P2_RIGHT -> p2Controls = new ControlScheme(p2Controls.getKey(PlayerAction.LEFT), newKey,
                    p2Controls.getKey(PlayerAction.JUMP), p2Controls.getKey(PlayerAction.DOWN),
                    p2Controls.getKey(PlayerAction.SHOOT));
            case P2_JUMP -> p2Controls = new ControlScheme(p2Controls.getKey(PlayerAction.LEFT),
                    p2Controls.getKey(PlayerAction.RIGHT), newKey, p2Controls.getKey(PlayerAction.DOWN),
                    p2Controls.getKey(PlayerAction.SHOOT));
            case P2_DOWN -> p2Controls = new ControlScheme(p2Controls.getKey(PlayerAction.LEFT),
                    p2Controls.getKey(PlayerAction.RIGHT), p2Controls.getKey(PlayerAction.JUMP), newKey,
                    p2Controls.getKey(PlayerAction.SHOOT));
            case P2_SHOOT -> p2Controls = new ControlScheme(p2Controls.getKey(PlayerAction.LEFT),
                    p2Controls.getKey(PlayerAction.RIGHT), p2Controls.getKey(PlayerAction.JUMP),
                    p2Controls.getKey(PlayerAction.DOWN), newKey);
            default -> {}

        }
    }
}
