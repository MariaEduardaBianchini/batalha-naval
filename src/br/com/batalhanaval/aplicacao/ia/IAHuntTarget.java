package br.com.batalhanaval.aplicacao.ia;

import br.com.batalhanaval.dominio.arma.Arma;
import br.com.batalhanaval.dominio.arma.TiroSimples;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.jogador.Jogador;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

/**
 * Esqueleto: após acerto, empilha vizinhos para tentar “finalizar”.
 * Por enquanto, se a fila estiver vazia, cai no aleatório.
 * (Para funcionar 100%, precisamos alimentar a fila com base em eventos de acerto — fica para evolução.)
 */
public class IAHuntTarget implements EstrategiaIA {

    private final Random rnd = new Random();
    private final Queue<Coordenada> filaAlvos = new ArrayDeque<>();

    @Override
    public RegistroJogada decidir(Jogador eu, Jogador oponente) {
        Embarcacao atacante = eu.frota().stream()
                .filter(e -> !e.estaAfundada())
                .findFirst().orElse(eu.frota().get(0));

        Arma arma = new TiroSimples();

        Coordenada alvo;
        if (!filaAlvos.isEmpty()) {
            alvo = filaAlvos.poll();
        } else {
            int l = rnd.nextInt(oponente.tabuleiro().linhas());
            int c = rnd.nextInt(oponente.tabuleiro().colunas());
            alvo = new Coordenada(l, c);
        }

        return new RegistroJogada(atacante, arma, alvo);
    }

    /* Exemplo de como você alimentaria a fila:
       chamar isso quando receber um EVENTO de ACERTO/AFUNDOU no tabuleiro do humano.
       (Integração via Observer é uma evolução do passo 8.)
    */
    public void enfileirarVizinhos(Coordenada centro, int maxLinhas, int maxColunas) {
        int l = centro.linha(), c = centro.coluna();
        if (l-1 >= 0)              filaAlvos.add(new Coordenada(l-1, c));
        if (l+1 < maxLinhas)       filaAlvos.add(new Coordenada(l+1, c));
        if (c-1 >= 0)              filaAlvos.add(new Coordenada(l, c-1));
        if (c+1 < maxColunas)      filaAlvos.add(new Coordenada(l, c+1));
    }
}
