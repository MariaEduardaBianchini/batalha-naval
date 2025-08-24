package br.com.batalhanaval.dominio.arma;

import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;

import java.util.List;

/**
 * Strategy: cada arma define como calcula seus alvos.
 */
public interface Arma {

    String nome();

    /**
     * Calcula as coordenadas afetadas, dado o centro informado.
     */
    List<Coordenada> calcularAlvos(Coordenada centro, Tabuleiro tabuleiro);
}
