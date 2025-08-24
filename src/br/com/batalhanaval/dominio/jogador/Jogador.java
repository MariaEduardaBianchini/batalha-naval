package br.com.batalhanaval.dominio.jogador;

import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;

import java.util.List;

public class Jogador {
    private final String nome;
    private final Tabuleiro tabuleiro;
    private final List<Embarcacao> frota;

    public Jogador(String nome, Tabuleiro tabuleiro, List<Embarcacao> frota) {
        this.nome = nome;
        this.tabuleiro = tabuleiro;
        this.frota = frota;
    }

    public String nome() { return nome; }
    public Tabuleiro tabuleiro() { return tabuleiro; }
    public List<Embarcacao> frota() { return frota; }
}
