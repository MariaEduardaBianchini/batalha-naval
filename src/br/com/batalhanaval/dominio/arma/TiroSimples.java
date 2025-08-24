package br.com.batalhanaval.dominio.arma;

import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;

import java.util.Collections;
import java.util.List;

public class TiroSimples implements Arma {

    @Override
    public String nome() {
        return "Tiro Simples";
    }

    @Override
    public List<Coordenada> calcularAlvos(Coordenada centro, Tabuleiro tabuleiro) {
        // Apenas a célula central
        return Collections.singletonList(centro);
    }
}
