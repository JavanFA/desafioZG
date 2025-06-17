package com.desafiozg.rpg.model;

import java.util.ArrayList;
import java.util.List;


public class Jogador {

    private String nome;
    private int vidaAtual;
    private int vidaMaxima;
    private int numeroSecreto; 
    private List<Item> artefatos;
    private boolean possuiEspadaZG;

    public Jogador(String nome) {
        this.nome = nome;
        this.vidaMaxima = 5;       
        this.vidaAtual = this.vidaMaxima; 
        this.artefatos = new ArrayList<>(); 
        this.possuiEspadaZG = false;      
    }


    /**
     * Aplica dano ao jogador, reduzindo sua vida atual.
     * @param dano A quantidade de vida a ser perdida.
     */
    public void receberDano(int dano) {
        this.vidaAtual -= dano;
        if (this.vidaAtual < 0) {
            this.vidaAtual = 0; 
        }
        System.out.println(this.nome + " recebeu " + dano + " de dano! Vida restante: " + this.vidaAtual);
    }

    /**
     * Verifica se o jogador ainda está vivo.
     * @return true se a vida atual for maior que 0, false caso contrário.
     */
    public boolean estaVivo() {
        return this.vidaAtual > 0;
    }

    /**
     * Aumenta a vida máxima do jogador (recompensa após vencer uma batalha).
     * @param valor O valor a ser somado à vida máxima.
     */
    public void aumentarVidaMaxima(int valor) {
        this.vidaMaxima += valor;
    }

    /**
     * Restaura completamente a vida do jogador para o seu valor máximo.
     */
    public void curarTotalmente() {
        this.vidaAtual = this.vidaMaxima;
    }

    /**
     * Adiciona um novo artefato à lista de itens do jogador.
     * @param nomeDoArtefato O nome do item a ser adicionado.
     */
    public void adicionarArtefato(Item novoArtefato) { // Antes era String
        this.artefatos.add(novoArtefato);
        System.out.println(this.nome + " adquiriu o artefato: " + novoArtefato.getNomeAmigavel() + "!");
    }
    


    public String getNome() {
        return nome;
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public List<Item> getArtefatos() { 
        return artefatos;
    }

    public void setNumeroSecreto(int numeroSecreto) {
        this.numeroSecreto = numeroSecreto;
    }

    public int getNumeroSecreto() {
        return numeroSecreto;
    }

    public boolean getPossuiEspadaZG() {
        return possuiEspadaZG;
    }

    public void setPossuiEspadaZG(boolean possuiEspadaZG) {
        this.possuiEspadaZG = possuiEspadaZG;
    }
}