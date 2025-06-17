package com.desafiozg;

import com.desafiozg.rpg.enums.ResultadoBatalha;
import com.desafiozg.rpg.game.Batalha;
import com.desafiozg.rpg.game.GerenciadorDeHistoria;
import com.desafiozg.rpg.game.ResultadoDeAcao;
import com.desafiozg.rpg.model.Inimigo;
import com.desafiozg.rpg.model.Item;
import com.desafiozg.rpg.model.Jogador;
import com.desafiozg.rpg.sound.SoundManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameController {

    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImageView;
    @FXML private Label nomeJogadorLabel;
    @FXML private Label vidaJogadorLabel;
    @FXML private Label artefatosLabel;
    @FXML private ImageView mapaImageView;
    @FXML private TextArea logTextArea;
    @FXML private HBox fasesBotoesBox;
    @FXML private Button florestaButton;
    @FXML private Button cavernasButton;
    @FXML private Button vilaButton;
    @FXML private Button torreButton;
    @FXML private Button chefeButton;
    @FXML private HBox acoesBatalhaBox;
    @FXML private HBox batalhaBotoesBox;
    @FXML private Button usarItemButton;
    @FXML private VBox painelInimigo;
    @FXML private ImageView inimigoImageView;
    @FXML private Label inimigoVidaLabel;
    
    private Jogador jogador;
    private GerenciadorDeHistoria gerenciador;
    private Batalha batalhaAtual;
    private int faseAtual;

    private Image backgroundMapa, backgroundFloresta, backgroundCaverna, backgroundVila, backgroundTorre, backgroundGlozium;
    private Image imgMonstrengo, imgUrso, imgDragao, imgEstatua, imgGlozium;

    @FXML
    public void initialize() {
        carregarImagens();
        backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
        backgroundImageView.setImage(backgroundMapa);
    }
    
    public void iniciarDadosDoJogador(String nomeDoJogador) {
        this.jogador = new Jogador(nomeDoJogador);
        this.gerenciador = new GerenciadorDeHistoria(this.jogador);
        
        SoundManager.playMusic("mapa.mp3");

        gerenciador.getTextoInicio();
        logTextArea.setText("A jornada se inicia... Escolha um destino no mapa.");
        atualizarInterface();
    }
    
    @FXML private void irParaFloresta() { SoundManager.playSound("click"); preparaFase(1, backgroundFloresta); }
    @FXML private void irParaCavernas() { SoundManager.playSound("click"); preparaFase(2, backgroundCaverna); }
    @FXML private void irParaVila() { SoundManager.playSound("click"); preparaFase(3, backgroundVila); }
    
    @FXML
    private void irParaTorre() {
        SoundManager.playSound("click");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Subir a Torre");
        alert.setHeaderText("A torre é alta demais para escalar.");
        alert.setContentText("Deseja usar a Azah Transmissão (Capa) para voar até o topo?");
        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gerenciador.usarCapaParaVoar();
            preparaFase(4, backgroundTorre);
        } else {
            exibirLog(List.of("Você decide não usar a capa agora."));
        }
    }
    
    private void preparaFase(int numeroFase, Image novoFundo) {
        this.faseAtual = numeroFase;
        backgroundImageView.setImage(novoFundo);
        mapaImageView.setVisible(false);
        Inimigo inimigo = gerenciador.getInimigoDaFase(numeroFase);
        mostrarPainelInimigo(true, inimigo, false);
        List<String> textos = gerenciador.getTextoIntroducao(numeroFase);
        exibirLog(textos);
        mostrarPaineis(false, true, false);
    }

    @FXML
    private void onLutar() {
        SoundManager.playSound("click");
        Inimigo inimigo = gerenciador.getInimigoDaFase(faseAtual);
        if (inimigo != null) {
            SoundManager.stopMusic();
            SoundManager.playMusic("batalha.mp3");
            this.batalhaAtual = new Batalha(jogador, inimigo);
            batalhaAtual.iniciarBatalha();
            inimigoVidaLabel.setVisible(true);
            atualizarInterface();
            exibirLog(List.of("A batalha começou! Escolha sua ação."));
            mostrarPaineis(false, false, true);
        }
    }

    @FXML
    private void onFugir() {
        SoundManager.playSound("click");
        SoundManager.stopMusic();
        SoundManager.playMusic("mapa.mp3");
        exibirLog(List.of("Você escolheu não lutar e recuou para a segurança do mapa."));
        finalizarAcao();
    }
    
    @FXML private void onAtacarNormal() { SoundManager.playSound("click"); executarAcaoDeBatalha(null); }
    
    @FXML
    private void onUsarItem() {
        SoundManager.playSound("click");
        List<Item> itensDisponiveis = jogador.getArtefatos();
        if (itensDisponiveis == null || itensDisponiveis.isEmpty()) return;
        ChoiceDialog<Item> dialog = new ChoiceDialog<>(itensDisponiveis.get(0), itensDisponiveis);
        dialog.setTitle("Inventário");
        dialog.setHeaderText("Qual artefato você deseja usar?");
        dialog.setContentText("Item:");
        Optional<Item> resultado = dialog.showAndWait();
        resultado.ifPresent(this::executarAcaoDeBatalha);
    }

    private void executarAcaoDeBatalha(Item item) {
        if (batalhaAtual == null) return;
        batalhaBotoesBox.setDisable(true);
        
        List<String> logJogador = batalhaAtual.executarTurnoJogador(item);
        exibirLog(logJogador);
        atualizarInterface();

        if (batalhaAtual.isBatalhaTerminada()) {
            processarFimDeBatalha();
        } else {
            PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));
            pausa.setOnFinished(event -> {
                List<String> logInimigo = batalhaAtual.executarTurnoInimigo();
                logTextArea.appendText("\n" + String.join("\n", logInimigo));
                if (batalhaAtual.isBatalhaTerminada()) {
                    processarFimDeBatalha();
                } else {
                    atualizarInterface();
                    batalhaBotoesBox.setDisable(false);
                }
            });
            pausa.play();
        }
    }
    
    private void processarFimDeBatalha() {
        SoundManager.stopMusic();
        if (jogador.estaVivo()) {
            if (faseAtual == 5) {
                try {
                    App.setRoot("victory_view");
                } catch (IOException e) { e.printStackTrace(); }
            } else {
                SoundManager.playMusic("vitoria.mp3");
                jogador.aumentarVidaMaxima(2);
                jogador.curarTotalmente();
                ResultadoDeAcao resultadoVitoria = gerenciador.processarVitoriaDaFase(faseAtual);
                logTextArea.appendText("\n" + String.join("\n", resultadoVitoria.textos()));
                if (gerenciador.podeForjarEspada()) {
                    mostrarPopupForjarEspada();
                }
                finalizarAcao();
            }
        } else {
            try {
                App.setRoot("game_over_view");
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    @FXML
    private void enfrentarGlozium() {
        SoundManager.playSound("click");
        if (!gerenciador.podeEnfrentarGlozium()) {
            exibirLog(gerenciador.getTextoBarreiraGlozium());
            return;
        }
        SoundManager.stopMusic();
        SoundManager.playMusic("batalhaFinal.mp3");
        backgroundImageView.setImage(backgroundGlozium);
        mapaImageView.setVisible(false);
        Inimigo glozium = gerenciador.getInimigoDaFase(5);
        this.faseAtual = 5;
        this.batalhaAtual = new Batalha(jogador, glozium);
        batalhaAtual.iniciarBatalha();
        mostrarPainelInimigo(true, glozium, true);
        exibirLog(List.of("Com a barreira quebrada, você encara Glozium! Lute por sua vida!"));
        mostrarPaineis(false, false, true);
    }

    private void mostrarPopupForjarEspada() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Forjar a Espada Lendária");
        alert.setHeaderText("Você reuniu todos os artefatos necessários!");
        alert.setContentText("Deseja combiná-los agora para forjar a Espada ZG?");
        ButtonType botaoForjar = new ButtonType("Forjar Espada");
        ButtonType botaoCancelar = new ButtonType("Ainda não");
        alert.getButtonTypes().setAll(botaoForjar, botaoCancelar);
        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == botaoForjar) {
            gerenciador.forjarEspadaZG();
            exibirLog(List.of("Você forja a poderosa Espada ZG!", "Todos os outros artefatos foram consumidos."));
            atualizarInterface();
        } else {
            exibirLog(List.of("Você decide aguardar o momento certo."));
        }
    }
    
    @FXML private void onSairDoJogo() { SoundManager.playSound("click"); Platform.exit(); }
    
    private void mostrarPaineis(boolean fases, boolean acoes, boolean combate) {
        fasesBotoesBox.setVisible(fases); fasesBotoesBox.setManaged(fases);
        acoesBatalhaBox.setVisible(acoes); acoesBatalhaBox.setManaged(acoes);
        batalhaBotoesBox.setVisible(combate); batalhaBotoesBox.setManaged(combate);
    }
    
    private void finalizarAcao() {
        SoundManager.stopMusic();
        SoundManager.playMusic("mapa.mp3");
        atualizarInterface();
        backgroundImageView.setImage(backgroundMapa);
        mapaImageView.setVisible(true);
        mostrarPainelInimigo(false, null, false);
        mostrarPaineis(true, false, false);
        batalhaBotoesBox.setDisable(false);
        batalhaAtual = null;
    }

    private void mostrarPainelInimigo(boolean mostrar, Inimigo inimigo, boolean mostrarVida) {
        painelInimigo.setVisible(mostrar);
        painelInimigo.setManaged(mostrar);
        if(mostrar && inimigo != null) {
            inimigoVidaLabel.setVisible(mostrarVida);
            inimigoVidaLabel.setText("Vida: " + inimigo.getVidaAtual() + " / " + inimigo.getVidaMaxima());
            Image imagemMonstro = null;
            switch(inimigo.getNome()) {
                case "Monstrengo": imagemMonstro = imgMonstrengo; break;
                case "Urso Sangrento": imagemMonstro = imgUrso; break;
                case "Dragão da Transmissão": imagemMonstro = imgDragao; break;
                case "Estátua do Último Herói": imagemMonstro = imgEstatua; break;
                case "Glozium, o Imortal": imagemMonstro = imgGlozium; break;
            }
            inimigoImageView.setImage(imagemMonstro);
        }
    }
    
    private void carregarImagens() {
        backgroundMapa = carregarUmaImagem("mapa.png");
        backgroundFloresta = carregarUmaImagem("background_floresta.png");
        backgroundCaverna = carregarUmaImagem("background_caverna.png");
        backgroundVila = carregarUmaImagem("background_vila.png");
        backgroundTorre = carregarUmaImagem("background_torre.png");
        backgroundGlozium = carregarUmaImagem("background_glozium.png");
        imgMonstrengo = carregarUmaImagem("monstrengo.png");
        imgUrso = carregarUmaImagem("urso_sangrento.png");
        imgDragao = carregarUmaImagem("dragao_transmissao.png");
        imgEstatua = carregarUmaImagem("estatua_heroi.png");
        imgGlozium = carregarUmaImagem("glozium.png");
    }

    private Image carregarUmaImagem(String nomeArquivo) {
        try {
            InputStream stream = getClass().getResourceAsStream("/assets/images/" + nomeArquivo);
            if (stream == null) {
                System.out.println("Erro de Recurso: Imagem '" + nomeArquivo + "' não encontrada!");
                return null;
            }
            return new Image(stream);
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem: " + nomeArquivo);
            e.printStackTrace();
            return null;
        }
    }
    
    private void exibirLog(List<String> textos) {
        logTextArea.clear();
        for (String linha : textos) {
            logTextArea.appendText(linha + "\n");
        }
    }

    private void atualizarInterface() {
        if (jogador == null) return;
        nomeJogadorLabel.setText("Jogador: " + jogador.getNome());
        vidaJogadorLabel.setText("Vida: " + jogador.getVidaAtual() + " / " + jogador.getVidaMaxima());
        double porcentagemVida = (double) jogador.getVidaAtual() / jogador.getVidaMaxima();
        vidaJogadorLabel.setStyle(porcentagemVida <= 0.5 ? "-fx-text-fill: #ff8a8a;" : "-fx-text-fill: #8aff8a;");
        String artefatosStr = jogador.getArtefatos().stream()
                                     .map(Item::getNomeAmigavel)
                                     .collect(Collectors.joining(", "));
        if (artefatosStr.isEmpty()) { artefatosStr = "Nenhum"; }
        artefatosLabel.setText("Artefatos: " + artefatosStr);
        usarItemButton.setDisable(jogador.getArtefatos().isEmpty());
        if (batalhaAtual != null && batalhaAtual.getInimigo() != null) {
            Inimigo inimigo = batalhaAtual.getInimigo();
            inimigoVidaLabel.setText("Vida: " + inimigo.getVidaAtual() + " / " + inimigo.getVidaMaxima());
            double porcentagemVidaInimigo = (double) inimigo.getVidaAtual() / inimigo.getVidaMaxima();
            inimigoVidaLabel.setStyle(porcentagemVidaInimigo <= 0.5 ? "-fx-text-fill: #ff8a8a;" : "-fx-text-fill: white;");
        }
        if(gerenciador != null) {
            florestaButton.setDisable(gerenciador.isFaseConcluida(1));
            cavernasButton.setDisable(gerenciador.isFaseConcluida(2));
            vilaButton.setDisable(gerenciador.isFaseConcluida(3));
            torreButton.setDisable(!gerenciador.possuiCapaParaVoar() || gerenciador.isFaseConcluida(4));
        }
    }
}