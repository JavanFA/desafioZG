package com.desafiozg.rpg.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static MediaPlayer musicPlayer;
    private static Map<String, AudioClip> soundEffects = new HashMap<>();

    public static void loadSounds() {
        try {
            AudioClip clickSound = new AudioClip(getResourceUrl("/assets/sounds/click.mp3"));
            clickSound.setVolume(0.5); 
            soundEffects.put("click", clickSound);

            AudioClip vitoriaSound = new AudioClip(getResourceUrl("/assets/sounds/vitoria.mp3"));
            vitoriaSound.setVolume(0.6); 
            soundEffects.put("vitoria", vitoriaSound);

        } catch (Exception e) {
            System.err.println("Erro ao carregar efeitos sonoros: " + e.getMessage());
        }
    }

    public static void playMusic(String filename) {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
        try {
            Media media = new Media(getResourceUrl("/assets/sounds/" + filename));
            musicPlayer = new MediaPlayer(media);
            
            musicPlayer.setVolume(0.3); 
            
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.play();
        } catch (Exception e) {
            System.err.println("Erro ao tocar música " + filename + ": " + e.getMessage());
        }
    }

    public static void playSound(String soundName) {
        AudioClip clip = soundEffects.get(soundName);
        if (clip != null) {
            clip.play();
        }
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    private static String getResourceUrl(String path) {
        URL resource = SoundManager.class.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("Recurso não encontrado: " + path);
        }
        return resource.toExternalForm();
    }
}