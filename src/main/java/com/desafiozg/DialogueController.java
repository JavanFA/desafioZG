package com.desafiozg;

import com.desafiozg.rpg.game.GerenciadorDeHistoria;
import com.desafiozg.rpg.model.Jogador;
import com.desafiozg.rpg.sound.SoundManager; 
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class DialogueController {

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImageView;
    @FXML private BorderPane dialoguePane;
    @FXML private ImageView speakerImageView;
    @FXML private TextArea dialogueTextArea;
    @FXML private Button acceptButton;

    private String nomeDoJogador;
    private boolean historiaDoPassadoContada = false;

    @FXML
    public void initialize() {
        backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
        
        try (InputStream stream = getClass().getResourceAsStream("/assets/images/vila_simples.png")) {
            if (stream != null) backgroundImageView.setImage(new Image(stream));
        } catch (Exception e) { e.printStackTrace(); }

        SoundManager.playMusic("prologo.mp3");
    }

    public void iniciarDialogo(String nomeJogador) {
        this.nomeDoJogador = nomeJogador;
        GerenciadorDeHistoria gerenciador = new GerenciadorDeHistoria(new Jogador(nomeDoJogador));
        
        List<String> textoPassado = gerenciador.getTextoPassado();
        dialogueTextArea.setText(String.join("\n\n", textoPassado));
        
        dialoguePane.setLeft(null);
        acceptButton.setText("Continuar...");
    }

    @FXML
    private void onAcceptMission() {
        SoundManager.playSound("click");

        if (!historiaDoPassadoContada) {
            GerenciadorDeHistoria gerenciador = new GerenciadorDeHistoria(new Jogador(nomeDoJogador));
            List<String> dialogoAvo = gerenciador.getTextoInicio();
            dialogueTextArea.setText(String.join("\n\n", dialogoAvo));

            dialoguePane.setLeft(speakerImageView);
            try (InputStream stream = getClass().getResourceAsStream("/assets/images/avo.png")) {
                if (stream != null) speakerImageView.setImage(new Image(stream));
            } catch (Exception e) { e.printStackTrace(); }
            
            acceptButton.setText("Aceitar Missão");
            historiaDoPassadoContada = true;
        } else {
            SoundManager.stopMusic();
            try {
                GameController gameController = App.setRoot("game_view");
                gameController.iniciarDadosDoJogador(this.nomeDoJogador);
            } catch (IOException e) {
                System.err.println("Erro ao carregar a tela do jogo (game_view.fxml):");
                e.printStackTrace();
            }
        }
    }
}