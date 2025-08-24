package br.com.batalhanaval.dominio.arma;

import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

/**
 * Afeta um bloco 3x3 centrado na coordenada escolhida.
 * Corrigido: valida limites ANTES de criar Coordenada.
 */
public class TiroExplosivo implements Arma {

    @Override
    public String nome() {
        return "Tiro Explosivo";
    }

    @Override
    public List<Coordenada> calcularAlvos(Coordenada centro, Tabuleiro tabuleiro) {
        List<Coordenada> alvos = new ArrayList<>();
        for (int dl = -1; dl <= 1; dl++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nl = centro.linha() + dl;
                int nc = centro.coluna() + dc;
                // cheque de limites ANTES de instanciar Coordenada
                if (nl >= 0 && nc >= 0 && nl < tabuleiro.linhas() && nc < tabuleiro.colunas()) {
                    alvos.add(new Coordenada(nl, nc));
                }
            }
        }
        return alvos;
    }
}
