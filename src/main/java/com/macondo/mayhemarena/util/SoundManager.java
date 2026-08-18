package com.macondo.mayhemarena.util;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, AudioClip> sounds;

    private SoundManager() {
        sounds = new HashMap<>();
        loadSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSounds() {
        try {
            URL shootUrl = getClass().getResource("/sounds/shoot.wav");
            URL reloadUrl = getClass().getResource("/sounds/reload.wav");
            URL hitUrl = getClass().getResource("/sounds/hit.wav");
            URL winUrl = getClass().getResource("/sounds/win.wav");

            if (shootUrl != null) {
                sounds.put("shoot", new AudioClip(shootUrl.toString()));
            }
            if (reloadUrl != null) {
                sounds.put("reload", new AudioClip(reloadUrl.toString()));
            }
            if (hitUrl != null) {
                sounds.put("hit", new AudioClip(hitUrl.toString()));
            }
            if (winUrl != null) {
                sounds.put("win", new AudioClip(winUrl.toString()));
            }
        } catch (Exception e) {
            System.out.println("Sound files not found. Running without sounds");
        }
    }

    public void playShoot() {
        AudioClip clip = sounds.get("shoot");
        if (clip != null) {
            clip.stop();
            clip.play(0.3);
        }
    }

    public void playReload() {
        AudioClip clip = sounds.get("reload");
        if (clip != null) {
            clip.stop();
            clip.play(0.4);
        }
    }

    public void playHit() {
        AudioClip clip = sounds.get("hit");
        if (clip != null) {
            clip.stop();
            clip.play(0.5);
        }
    }

    public void playWin() {
            AudioClip clip = sounds.get("win");
            if (clip != null) {
                clip.stop();
                clip.play(0.7);
            }
    }
}
