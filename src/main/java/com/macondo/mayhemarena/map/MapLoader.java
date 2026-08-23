package com.macondo.mayhemarena.map;

import com.macondo.mayhemarena.entity.Platform;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MapLoader {
    private List<Platform> platforms;
    private String currentMap;

    public MapLoader() {
        platforms = new ArrayList<>();
        currentMap = "Sky Ruins";
    }

    public void loadMap(String mapName) {
        platforms.clear();
        currentMap = mapName;

        switch (mapName) {
            case "Sky Ruins":
                loadSkyRuins();
                break;
            case "Split Foundry":
                loadSplitFoundry();
                break;
            case "Twin Pits":
                loadTwinPits();
                break;
            case "Crystal Cavern":
                loadCrystalCavern();
                break;
            default:
                loadSkyRuins();
                break;
        }
    }

    private void loadSkyRuins() {
        platforms.add(new Platform(0, 654, 380, 66, false, Color.web("#7c5a38")));
        platforms.add(new Platform(500, 654, 280, 66, false, Color.web("#7c5a38")));
        platforms.add(new Platform(960, 654, 320, 66, false, Color.web("#7c5a38")));
        platforms.add(new Platform(120, 550, 260, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(510, 430, 220, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(900, 520, 250, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(310, 300, 180, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(760, 260, 190, 18, true, Color.web("#4b5563")));
    }

    private void loadSplitFoundry() {
        platforms.add(new Platform(0, 654, 250, 66, false, Color.web("#6b3f24")));
        platforms.add(new Platform(360, 654, 240, 66, false, Color.web("#6b3f24")));
        platforms.add(new Platform(730, 654, 210, 66, false, Color.web("#6b3f24")));
        platforms.add(new Platform(1040, 654, 240, 66, false, Color.web("#6b3f24")));
        platforms.add(new Platform(180, 510, 190, 18, true, Color.web("#52525b")));
        platforms.add(new Platform(445, 395, 160, 18, true, Color.web("#52525b")));
        platforms.add(new Platform(670, 505, 180, 18, true, Color.web("#52525b")));
        platforms.add(new Platform(930, 350, 210, 18, true, Color.web("#52525b")));
        platforms.add(new Platform(540, 250, 180, 18, true, Color.web("#52525b")));
    }

    private void loadTwinPits() {
        platforms.add(new Platform(0, 654, 300, 66, false, Color.web("#6b553c")));
        platforms.add(new Platform(420, 654, 200, 66, false, Color.web("#6b553c")));
        platforms.add(new Platform(760, 654, 180, 66, false, Color.web("#6b553c")));
        platforms.add(new Platform(1080, 654, 200, 66, false, Color.web("#6b553c")));
        platforms.add(new Platform(190, 520, 170, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(400, 470, 150, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(600, 340, 140, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(810, 470, 150, 18, true, Color.web("#4b5563")));
        platforms.add(new Platform(990, 520, 170, 18, true, Color.web("#4b5563")));
    }

    private void loadCrystalCavern() {
        platforms.add(new Platform(0, 654, 340, 66, false, Color.web("#4c3f69")));
        platforms.add(new Platform(940, 654, 340, 66, false, Color.web("#4c3f69")));
        platforms.add(new Platform(570, 590, 140, 18, true, Color.web("#536878")));
        platforms.add(new Platform(140, 500, 180, 18, true, Color.web("#536878")));
        platforms.add(new Platform(960, 500, 180, 18, true, Color.web("#536878")));
        platforms.add(new Platform(360, 420, 160, 18, true, Color.web("#536878")));
        platforms.add(new Platform(760, 420, 160, 18, true, Color.web("#536878")));
        platforms.add(new Platform(570, 330, 140, 18, true, Color.web("#536878")));
        platforms.add(new Platform(250, 260, 120, 18, true, Color.web("#536878")));
        platforms.add(new Platform(910, 260, 120, 18, true, Color.web("#536878")));
    }

    public double[] getSpawnPositions() {
        switch (currentMap) {
            case "Sky Ruins":
                return new double[]{150, 590, 1060, 590};
            case "Split Foundry":
                return new double[]{120, 590, 1110, 590};
            case "Twin Pits":
                return new double[]{140, 590, 1120, 590};
            case "Crystal Cavern":
                return new double[]{170, 590, 1080, 590};
            default:
                return new double[]{500, 360,780, 360};
        }
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public Platform getGround() {
        return platforms.isEmpty() ? null : platforms.get(0);
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public String[] getMapNames() {
        return new String[]{"Sky Ruins", "Split Foundry", "Twin Pits", "Crystal Cavern"};
    }


}
