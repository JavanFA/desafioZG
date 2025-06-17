package com.desafiozg;

import java.io.IOException;
import com.desafiozg.rpg.sound.SoundManager;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class VictoryController {

    @FXML
    public void initialize() {
        SoundManager.playMusic("vitoria.mp3");
    }

    @FXML
    private void onJogarNovamente() {
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