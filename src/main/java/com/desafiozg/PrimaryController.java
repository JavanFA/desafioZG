package com.desafiozg;

import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import com.desafiozg.rpg.sound.SoundManager;


public class PrimaryController {

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImageView;
    @FXML private TextField nomeTextField;

    @FXML
    public void initialize() {
        backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
        
        try (InputStream stream = getClass().getResourceAsStream("/assets/images/tela_inicial.png")) {
            if (stream != null) {
                backgroundImageView.setImage(new Image(stream));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SoundManager.playMusic("inicio.mp3");
    }

    @FXML
    private void iniciarJornada() {
        SoundManager.playSound("click");
        SoundManager.stopMusic();
        String nomeDoJogador = nomeTextField.getText();
        if (nomeDoJogador == null || nomeDoJogador.trim().isEmpty()) {
            nomeDoJogador = "Sandubinha";
        }
        
        try {
            DialogueController dialogueController = App.setRoot("dialogue_view");
            dialogueController.iniciarDialogo(nomeDoJogador);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}