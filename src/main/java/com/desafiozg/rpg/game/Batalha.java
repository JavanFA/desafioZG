package com.desafiozg.rpg.game;

import com.desafiozg.rpg.model.Inimigo;
import com.desafiozg.rpg.model.Item;
import com.desafiozg.rpg.model.Jogador;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Batalha {

    private Jogador jogador;
    private Inimigo inimigo;
    private Random random;
    private boolean bonusDanoInimigo = false;
    private int errosEstilingue = 0;
    private int errosConsecutivosJogador = 0;

    public Batalha(Jogador jogador, Inimigo inimigo) {
        this.jogador = jogador;
        this.inimigo = inimigo;
        this.random = new Random();
    }

    public void iniciarBatalha() {
        jogador.setNumeroSecreto(random.nextInt(jogador.getVidaMaxima()) + 1);
        inimigo.setNumeroSecreto(random.nextInt(inimigo.getVidaMaxima()) + 1);
    }

    public List<String> executarTurnoJogador(Item itemEscolhido) {
        List<String> logDoTurno = new ArrayList<>();
        logDoTurno.add(">>> Seu Turno <<<");
        if (itemEscolhido != null) logDoTurno.add("Você usa " + itemEscolhido.getNomeAmigavel() + "!");
        if (itemEscolhido == Item.COLAR_DA_ESTATUA_SAGRADA) {
            logDoTurno.add("O colar drena 3 de sua vida!");
            jogador.receberDano(3);
            if (!jogador.estaVivo()) {
                logDoTurno.add("O poder do colar foi demais para você...");
                return logDoTurno;
            }
        }
        int numerosParaSortear = (itemEscolhido != null) ? getNumerosSorteadosPorItem(itemEscolhido) : 2;
        logDoTurno.add("Você ataca sorteando " + numerosParaSortear + " número(s)...");
        
        int acertosJogador = 0;
        if (errosConsecutivosJogador >= 3) {
            logDoTurno.add("Sua determinação garante um acerto!");
            acertosJogador = 1;
        } else {
            for (int i = 0; i < numerosParaSortear; i++) {
                if (random.nextInt(inimigo.getVidaMaxima()) + 1 == inimigo.getNumeroSecreto()) acertosJogador++;
            }
        }

        if (acertosJogador > 0) {
            errosConsecutivosJogador = 0;
            int dano = calcularDano(itemEscolhido, acertosJogador, inimigo.getNumeroSecreto());
            logDoTurno.add("💥 Acerto Crítico! Você causou " + dano + " de dano.");
            inimigo.receberDano(dano);
            if (itemEscolhido == Item.ESTILINGUE_MAGICO) {
                logDoTurno.add("O impacto do estilingue atordoou o inimigo!");
                inimigo.setAtordoado(true);
            }
        } else {
            errosConsecutivosJogador++;
            logDoTurno.add("💨 Você errou o ataque. Erros seguidos: " + errosConsecutivosJogador);
            if (itemEscolhido == Item.FATURAMENTUS) {
                logDoTurno.add("Sua falha deixou uma abertura! O próximo ataque do inimigo será mais forte.");
                this.bonusDanoInimigo = true;
            }
            if (itemEscolhido == Item.ESTILINGUE_MAGICO) {
                if (this.errosEstilingue >= 3) {
                    logDoTurno.add("Sua mira com o estilingue está instável! Você perde 1 de vida.");
                    jogador.receberDano(1);
                } else {
                    this.errosEstilingue++;
                }
            }
        }
        logDoTurno.add("Vida do inimigo: " + inimigo.getVidaAtual() + "/" + inimigo.getVidaMaxima());
        return logDoTurno;
    }

    public List<String> executarTurnoInimigo() {
        List<String> logDoTurno = new ArrayList<>();
        logDoTurno.add("\n<<< Turno de " + inimigo.getNome() + " >>>");
        if(inimigo.isAtordoado()){
            logDoTurno.add(inimigo.getNome() + " está atordoado e não pode atacar!");
            inimigo.setAtordoado(false);
        } else {
            int acertosInimigo = (random.nextInt(jogador.getVidaMaxima()) + 1 == jogador.getNumeroSecreto()) ? 1 : 0;
            if (acertosInimigo > 0) {
                int dano = jogador.getNumeroSecreto();
                if (this.bonusDanoInimigo) {
                    logDoTurno.add("O inimigo explora sua abertura e causa dano extra!");
                    dano += 2;
                    this.bonusDanoInimigo = false;
                }
                logDoTurno.add("🩸 Dano Crítico! O inimigo causou " + dano + " de dano em você.");
                jogador.receberDano(dano);
            } else {
                logDoTurno.add("🛡️ O inimigo errou o ataque.");
            }
        }
        logDoTurno.add("Sua vida: " + jogador.getVidaAtual() + "/" + jogador.getVidaMaxima());
        return logDoTurno;
    }

    private int calcularDano(Item item, int acertos, int numSecreto) {
        if (item == Item.ESPADA_ZG) return acertos * numSecreto;
        if (item == Item.GUIA_DE_ATENDIMENTO) return acertos;
        return acertos * numSecreto;
    }
    
    private int getNumerosSorteadosPorItem(Item item) {
        switch (item) {
            case GUIA_DE_ATENDIMENTO: return 2;
            case FATURAMENTUS: return 4;
            case ESTILINGUE_MAGICO: return 1 + 5;
            case AZAH_TRANSMISSAO: case COLAR_DA_ESTATUA_SAGRADA: return 1 + 10;
            case ESPADA_ZG: return 40;
            default: return 2;
        }
    }
    
    public boolean isBatalhaTerminada() { return !jogador.estaVivo() || !inimigo.estaVivo(); }
    public Inimigo getInimigo() { return this.inimigo; }
}