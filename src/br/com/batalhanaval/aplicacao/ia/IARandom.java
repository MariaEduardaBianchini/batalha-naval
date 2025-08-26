package br.com.batalhanaval.aplicacao.ia;

import br.com.batalhanaval.dominio.arma.Arma;
import br.com.batalhanaval.dominio.arma.TiroSimples;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.jogador.Jogador;

import java.util.Random;

/** IA básica: escolhe um navio vivo, arma simples e alvo aleatório. */
public class IARandom implements EstrategiaIA {
    private final Random rnd = new Random();

    @Override
    public RegistroJogada decidir(Jogador eu, Jogador oponente) {
        Embarcacao atacante = eu.frota().stream()
                .filter(e -> !e.estaAfundada())
                .findFirst().orElse(eu.frota().get(0));

        Arma arma = new TiroSimples(); // ilimitado e sempre permitido

        int l = rnd.nextInt(oponente.tabuleiro().linhas());
        int c = rnd.nextInt(oponente.tabuleiro().colunas());

        return new RegistroJogada(atacante, arma, new Coordenada(l, c));
    }
}
