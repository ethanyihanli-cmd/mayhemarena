package com.macondo.mayhemarena.util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, AudioClip> sounds;
    private MediaPlayer backgroundPlayer;

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
            URL shootUrl = getClass().getResource("/com/macondo/mayhemarena/sounds/shoot.wav");
            URL reloadUrl = getClass().getResource("/com/macondo/mayhemarena/sounds/reload.wav");
            URL hitUrl = getClass().getResource("/com/macondo/mayhemarena/sounds/hit.wav");
            URL winUrl = getClass().getResource("/com/macondo/mayhemarena/sounds/win.wav");

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

    public void playBackgroundMusic(String fileName) {
        try {
            URL url = getClass().getResource("/com/macondo/mayhemarena/sounds/" + fileName);
            if (url == null) {
                return;
            }
            if (backgroundPlayer != null) {
                backgroundPlayer.stop();
            }
            backgroundPlayer = new MediaPlayer(new Media(url.toString()));
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundPlayer.setVolume(0.22);
            backgroundPlayer.play();
        } catch (Exception e) {
            System.out.println("Background music could not be played");
        }
    }
}
