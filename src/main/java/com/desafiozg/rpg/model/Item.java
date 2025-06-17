package com.desafiozg.rpg.model;


public enum Item {
    GUIA_DE_ATENDIMENTO("Guia de Atendimento"),
    FATURAMENTUS("Faturamentus"),
    ESTILINGUE_MAGICO("Estilingue Mágico"),
    AZAH_TRANSMISSAO("Azah Transmissão"),
    COLAR_DA_ESTATUA_SAGRADA("Colar da Estátua Sagrada"),
    ESPADA_ZG("Espada ZG");

    private final String nomeAmigavel;

    Item(String nomeAmigavel) {
        this.nomeAmigavel = nomeAmigavel;
    }

    public String getNomeAmigavel() {
        return nomeAmigavel;
    }
}