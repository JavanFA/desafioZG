package com.desafiozg.rpg.game;

import com.desafiozg.rpg.enums.ResultadoBatalha;
import java.util.List;
import java.util.Objects;


public final class ResultadoDeAcao {
    private final ResultadoBatalha resultado;
    private final List<String> textos;

    public ResultadoDeAcao(ResultadoBatalha resultado, List<String> textos) {
        this.resultado = resultado;
        this.textos = textos;
    }

    public ResultadoBatalha resultado() {
        return resultado;
    }

    public List<String> textos() {
        return textos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ResultadoDeAcao) obj;
        return Objects.equals(this.resultado, that.resultado) &&
               Objects.equals(this.textos, that.textos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultado, textos);
    }

    @Override
    public String toString() {
        return "ResultadoDeAcao[" +
               "resultado=" + resultado + ", " +
               "textos=" + textos + ']';
    }
}