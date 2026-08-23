package com.macondo.mayhemarena.map;

import com.macondo.mayhemarena.entity.Platform;

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

        platforms.add(new Platform(0, 600, 1280, 20));

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
        platforms.add(new Platform(100, 500, 160, 20));
        platforms.add(new Platform(400, 460, 120, 20));
        platforms.add(new Platform(700, 520, 180, 20));
        platforms.add(new Platform(1050, 480, 140, 70));

        platforms.add(new Platform(250, 380, 100, 20));
        platforms.add(new Platform(850, 400, 120, 20));
    }

    private void loadSplitFoundry() {
        platforms.add(new Platform(0, 520, 500, 20));
        platforms.add(new Platform(780, 520, 500, 20));

        platforms.add(new Platform(550, 470, 180, 20));

        platforms.add(new Platform(200, 420, 120, 20));
        platforms.add(new Platform(960, 420, 120, 20));
        platforms.add(new Platform(350, 350, 100, 20));
        platforms.add(new Platform(830, 350, 100, 20));
    }

    private void loadTwinPits() {
        platforms.add(new Platform(0, 550, 350, 20));
        platforms.add(new Platform(465, 550, 350, 20));
        platforms.add(new Platform(930, 550, 350, 20));

        platforms.add(new Platform(380, 500, 70, 20));
        platforms.add(new Platform(830, 500, 70, 20));

        platforms.add(new Platform(140, 430, 120, 20));
        platforms.add(new Platform(600, 440, 100, 20));
        platforms.add(new Platform(1020, 430, 120, 20));
        platforms.add(new Platform(300, 340, 80, 20));
        platforms.add(new Platform(900, 340, 80, 20));
    }

    private void loadCrystalCavern() {
        platforms.add(new Platform(0, 570, 350, 20));
        platforms.add(new Platform(930, 570, 350, 20));
        platforms.add(new Platform(600, 540, 80, 20));
        platforms.add(new Platform(150, 480, 120, 20));
        platforms.add(new Platform(1010, 480, 120, 20));
        platforms.add(new Platform(350, 430, 100, 20));
        platforms.add(new Platform(830, 430, 100, 20));
        platforms.add(new Platform(600, 380, 80, 20));
        platforms.add(new Platform(250, 330, 80, 20));
        platforms.add(new Platform(950, 330, 80, 20));
    }

    public double[] getSpawnPositions() {
        switch (currentMap) {
            case "Sky Ruins":
                return new double[]{500, 360, 780, 360};
            case "Split Foundry":
                return new double[]{250, 400, 1030, 400};
            case "Twin Pits":
                return new double[]{175, 430, 625, 430};
            case "Crystal Cavern":
                return new double[]{300, 380, 980, 380};
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
