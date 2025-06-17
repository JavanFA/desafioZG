package com.desafiozg.rpg.game;

import com.desafiozg.rpg.enums.ResultadoBatalha;
import com.desafiozg.rpg.model.Inimigo;
import com.desafiozg.rpg.model.Item;
import com.desafiozg.rpg.model.Jogador;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GerenciadorDeHistoria {

    private Jogador jogador;
    private Set<Integer> fasesConcluidas = new HashSet<>();

    public GerenciadorDeHistoria(Jogador jogador) {
        this.jogador = jogador;
    }

    public List<String> getTextoPassado() {
        return List.of(
            "Passado...",
            "Vindo de onde os olhos não conseguiam alcançar, com um som alto, passando pelas montanhas e vales, pelos rios e florestas. Todos no mundo puderam ouvir uma voz que parecia um estrondo, que dizia:",
            "- Contemplem seu novo mestre, Glozium...",
            "- Demonstrativus potentiaaam!!",
            "O terror recaiu sobre as pessoas e o mundo foi tomado por uma neblina que provocou todo tipo de mazela. Alguns morreram, outros tornaram-se monstros, animais sofreram mutações."
        );
    }
    
    public List<String> getTextoInicio() {
        jogador.adicionarArtefato(Item.GUIA_DE_ATENDIMENTO);
        return List.of(
            "Seu avô se aproxima, com um olhar preocupado mas determinado.",
            "Avô: Meu neto, o fardo de deter Glozium agora é seu. Leve este Guia do Atendimento. É um artefato antigo de nossa família.",
            "\n[Você recebeu o 'Guia de Atendimento'!]"
        );
    }

    public List<String> getTextoIntroducao(int fase) {
        List<String> textos = new ArrayList<>();
        switch (fase) {
            case 1: textos.add("Você entra na Floresta do Atendimento e um Monstrengo surge!"); break;
            case 2: textos.add("Você adentra as Cavernas de Faturamento e encontra um Urso Sangrento."); break;
            case 3: textos.add("Você chega na Vila da Transmissão e um Dragão aparece!"); break;
            case 4: textos.add("Você alcança a Torre e uma Estátua do Último Herói ganha vida!"); break;
        }
        textos.add("\nO que você faz?");
        return textos;
    }

    public Inimigo getInimigoDaFase(int numeroFase) {
        switch (numeroFase) {
            case 1: return new Inimigo("Monstrengo", 3);
            case 2: return new Inimigo("Urso Sangrento", 6);
            case 3: return new Inimigo("Dragão da Transmissão", 12);
            case 4: return new Inimigo("Estátua do Último Herói", 25);
            case 5: return new Inimigo("Glozium, o Imortal", 100);
            default: return null;
        }
    }

    public ResultadoDeAcao processarVitoriaDaFase(int numeroFase) {
        this.fasesConcluidas.add(numeroFase);
        String textoVitoria = "";
        Item recompensa = null;
        switch (numeroFase) {
            case 1: recompensa = Item.FATURAMENTUS; textoVitoria = "Você venceu! Ganhou o 'Faturamentus'."; break;
            case 2: recompensa = Item.ESTILINGUE_MAGICO; textoVitoria = "Você venceu! Ganhou um 'Estilingue Mágico'."; break;
            case 3: recompensa = Item.AZAH_TRANSMISSAO; textoVitoria = "Você libertou o dragão! Ele te entrega a 'Azah Transmissão' (Capa)."; break;
            case 4: recompensa = Item.COLAR_DA_ESTATUA_SAGRADA; textoVitoria = "A estátua se despedaça, revelando o 'Colar da Estátua Sagrada'."; break;
        }
        if (recompensa != null) {
            jogador.adicionarArtefato(recompensa);
        }
        return new ResultadoDeAcao(ResultadoBatalha.VITORIA, List.of(textoVitoria));
    }
    
    public boolean podeForjarEspada() {
        return jogador.getArtefatos().containsAll(List.of(
            Item.GUIA_DE_ATENDIMENTO,
            Item.FATURAMENTUS, 
            Item.ESTILINGUE_MAGICO, 
            Item.AZAH_TRANSMISSAO, 
            Item.COLAR_DA_ESTATUA_SAGRADA
        ));
    }

    public void forjarEspadaZG() {
        jogador.getArtefatos().clear();
        jogador.adicionarArtefato(Item.ESPADA_ZG);
        jogador.setPossuiEspadaZG(true);
    }
    
    public boolean possuiCapaParaVoar() { return jogador.getArtefatos().contains(Item.AZAH_TRANSMISSAO); }
    
    public void usarCapaParaVoar() {
        System.out.println("Jogador usou a capa para voar para a torre.");
    }
    
    public boolean isFaseConcluida(int numeroFase) {
        return this.fasesConcluidas.contains(numeroFase);
    }
    
    public boolean podeEnfrentarGlozium() {
        return this.fasesConcluidas.size() >= 4;
    }
    
    public List<String> getTextoBarreiraGlozium() {
        return List.of("Uma barreira mística protege Glozium...", "Você precisa concluir as 4 regiões para quebrá-la.");
    }
    
    public List<String> getTextoFinal(boolean usouEspadaZG) {
        if (usouEspadaZG) {
            return List.of("Com o poder da Espada ZG, Glozium é apagado da existência. Você salvou o mundo!");
        } else {
            return List.of("Você derrotou Glozium, mas sem a espada, ele retornará. Seu sacrifício o transforma em pedra...");
        }
    }
}