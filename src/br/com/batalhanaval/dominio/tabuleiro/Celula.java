package br.com.batalhanaval.dominio.tabuleiro;

import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;

/**
 * Casa do tabuleiro.
 * Agora mantém referência para a Embarcacao ocupante (se houver).
 */
public class Celula {

    private final Coordenada coordenada;
    private boolean atingida;
    private Embarcacao ocupante; // null = vazia

    public Celula(Coordenada coordenada) {
        this.coordenada = coordenada;
        this.atingida = false;
        this.ocupante = null;
    }

    public Coordenada coordenada() { return coordenada; }

    public boolean atingida() { return atingida; }
    public void marcarAtingida() { this.atingida = true; }

    /** Retorna a embarcação que ocupa a célula (ou null se vazia). */
    public Embarcacao ocupante() { return ocupante; }

    /** True se há uma embarcação nesta célula. */
    public boolean temOcupante() { return ocupante != null; }

    /** Usado pelo Tabuleiro ao posicionar embarcações. */
    void definirOcupante(Embarcacao embarcacao) {
        this.ocupante = embarcacao;
    }

    @Override
    public String toString() {
        // Convenção de debug: "." = vazio, "O" = ocupado, "X" = atingida
        if (atingida) return "X";
        if (temOcupante()) return "O";
        return ".";
    }
}
