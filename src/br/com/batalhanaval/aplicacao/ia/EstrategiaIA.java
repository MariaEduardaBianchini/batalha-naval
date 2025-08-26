package br.com.batalhanaval.aplicacao.ia;

import br.com.batalhanaval.dominio.arma.Arma;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.jogador.Jogador;

/** Define como a IA escolhe atacante, arma e alvo. */
public interface EstrategiaIA {
    RegistroJogada decidir(Jogador eu, Jogador oponente);

    record RegistroJogada(Embarcacao atacante, Arma arma, Coordenada alvo) {}
}
