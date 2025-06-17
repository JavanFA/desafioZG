package com.desafiozg.rpg.model;

public class Inimigo {

    private String nome;
    private int vidaAtual;
    private int vidaMaxima;
    private int numeroSecreto;
    private boolean atordoado = false;

    public Inimigo(String nome, int vidaMaxima) {
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = this.vidaMaxima;
    }

    public void receberDano(int dano) {
        this.vidaAtual -= dano;
        if (this.vidaAtual < 0) {
            this.vidaAtual = 0;
        }
    }

    public boolean estaVivo() {
        return this.vidaAtual > 0;
    }


    public String getNome() { return nome; }
    public int getVidaAtual() { return vidaAtual; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getNumeroSecreto() { return numeroSecreto; }
    public void setNumeroSecreto(int numeroSecreto) { this.numeroSecreto = numeroSecreto; }

    public boolean isAtordoado() {
        return atordoado;
    }

    public void setAtordoado(boolean atordoado) {
        this.atordoado = atordoado;
    }
}