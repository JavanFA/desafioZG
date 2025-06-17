package com.desafiozg;

import java.io.IOException;
import com.desafiozg.rpg.sound.SoundManager;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class GameOverController {

    @FXML
    public void initialize() {
        SoundManager.playMusic("derrota.mp3");
    }

    @FXML
    private void onContinuar() {
        SoundManager.playSound("click");
        SoundManager.stopMusic();
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSair() {
        SoundManager.playSound("click");
        SoundManager.stopMusic();
        Platform.exit();
    }
}