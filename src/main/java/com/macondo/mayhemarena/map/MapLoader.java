package com.macondo.mayhemarena.map;

import com.macondo.mayhemarena.entity.Platform;

import java.util.ArrayList;
import java.util.List;

public class MapLoader {
    private List<Platform> platforms;

    public class MapLoader {
        platforms = new ArrayList<>();
    }

    public void loadMap(String mapName) {
        platforms.clear();

        platforms.add(new Platform(0, 600, 1280, 120));

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
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public Platform getGround() {
        return platforms.isEmpty() ? null : platforms.get(0);
    }

}
