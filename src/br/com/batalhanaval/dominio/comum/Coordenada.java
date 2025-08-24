package br.com.batalhanaval.dominio.comum;

/** Representa uma posição no tabuleiro (linha, coluna) iniciando em 0. */
public record Coordenada(int linha, int coluna) {
    public Coordenada {
        if (linha < 0 || coluna < 0) throw new IllegalArgumentException();
    }
}
