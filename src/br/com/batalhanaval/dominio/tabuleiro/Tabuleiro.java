package br.com.batalhanaval.dominio.tabuleiro;

import br.com.batalhanaval.dominio.arma.Arma;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.comum.Orientacao;
import br.com.batalhanaval.dominio.comum.ResultadoTiro;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.infraestrutura.ConfiguracaoJogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Matriz de células + regras de posicionamento.
 */
public class Tabuleiro {

    private final int linhas;
    private final int colunas;
    private final Celula[][] grade;

    public Tabuleiro(int linhas, int colunas) {
        if (linhas <= 0 || colunas <= 0) {
            throw new IllegalArgumentException("Dimensões inválidas.");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        this.grade = new Celula[linhas][colunas];
        inicializar();
    }

    public static Tabuleiro criarPorDificuldade(Dificuldade dificuldade) {
        int n = ConfiguracaoJogo.instancia().dimensaoQuadrada(dificuldade);
        return new Tabuleiro(n, n);
    }

    private void inicializar() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                grade[i][j] = new Celula(new Coordenada(i, j));
            }
        }
    }

    public boolean dentroDosLimites(Coordenada c) {
        return c.linha() >= 0 && c.linha() < linhas
                && c.coluna() >= 0 && c.coluna() < colunas;
    }

    public Optional<Celula> celulaEm(Coordenada c) {
        if (!dentroDosLimites(c)) return Optional.empty();
        return Optional.of(grade[c.linha()][c.coluna()]);
    }

    public int linhas() { return linhas; }
    public int colunas() { return colunas; }

    /** Renderização simples para debug. */
    public String desenharComoTexto() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                sb.append(grade[i][j]).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /* ========= POSICIONAMENTO DE EMBARCAÇÕES ========= */

    /** Calcula a lista de coordenadas que a embarcação ocuparia dada origem+orientação. */
    private List<Coordenada> calcularFaixa(Embarcacao embarcacao, Coordenada origem, Orientacao orientacao) {
        int dl = (orientacao == Orientacao.VERTICAL) ? 1 : 0;
        int dc = (orientacao == Orientacao.HORIZONTAL) ? 1 : 0;

        List<Coordenada> faixa = new ArrayList<>(embarcacao.tamanho());
        for (int k = 0; k < embarcacao.tamanho(); k++) {
            Coordenada c = new Coordenada(origem.linha() + k * dl, origem.coluna() + k * dc);
            faixa.add(c);
        }
        return faixa;
    }

    /** Verifica se é possível posicionar (dentro dos limites e sem sobreposição). */
    public boolean podePosicionar(Embarcacao embarcacao, Coordenada origem, Orientacao orientacao) {
        List<Coordenada> faixa = calcularFaixa(embarcacao, origem, orientacao);
        for (Coordenada c : faixa) {
            if (!dentroDosLimites(c)) return false;
            Celula cel = grade[c.linha()][c.coluna()];
            if (cel.temOcupante()) return false;
        }
        return true;
    }

    /**
     * Posiciona a embarcação.
     * Lança IllegalArgumentException se não puder (fora do tabuleiro ou sobreposição).
     */
    public void posicionar(Embarcacao embarcacao, Coordenada origem, Orientacao orientacao) {
        if (!podePosicionar(embarcacao, origem, orientacao)) {
            throw new IllegalArgumentException("Não é possível posicionar " + embarcacao.nome()
                    + " em " + origem + " (" + orientacao + ").");
        }
        List<Coordenada> faixa = calcularFaixa(embarcacao, origem, orientacao);

        // Marca ocupação na grade e grava as posições na embarcação
        for (Coordenada c : faixa) {
            grade[c.linha()][c.coluna()].definirOcupante(embarcacao);
        }
        embarcacao.definirPosicoes(faixa);
    }

    /**
     * Utilitário para posicionar uma frota inteira aleatoriamente (útil para testes/PC).
     * Estratégia simples de tentativas; suficiente para os tamanhos exigidos.
     */
    public void posicionarAleatorio(List<Embarcacao> frota, long seedOpcional) {
        Random rnd = (seedOpcional == 0L) ? new Random() : new Random(seedOpcional);
        for (Embarcacao e : frota) {
            boolean colocado = false;
            int tentativas = 0;
            while (!colocado) {
                tentativas++;
                if (tentativas > 10_000) {
                    throw new IllegalStateException("Falha ao posicionar frota (muitas tentativas).");
                }
                int linha = rnd.nextInt(linhas);
                int coluna = rnd.nextInt(colunas);
                Orientacao orient = (rnd.nextBoolean()) ? Orientacao.HORIZONTAL : Orientacao.VERTICAL;

                Coordenada origem = new Coordenada(linha, coluna);
                if (podePosicionar(e, origem, orient)) {
                    posicionar(e, origem, orient);
                    colocado = true;
                }
            }
        }
    }
    /**
     * Aplica um tiro no tabuleiro do inimigo.
     * Marca células atingidas e atualiza embarcações.
     * Retorna uma lista de resultados (um por célula afetada).
     */
    public List<ResultadoTiro> aplicarTiro(Arma arma, Coordenada alvo) {
        List<ResultadoTiro> resultados = new ArrayList<>();
        List<Coordenada> alvos = arma.calcularAlvos(alvo, this);

        for (Coordenada c : alvos) {
            if (!dentroDosLimites(c)) continue;

            Celula cel = grade[c.linha()][c.coluna()];
            cel.marcarAtingida();

            if (cel.temOcupante()) {
                boolean acertou = cel.ocupante().registrarAcerto(c);
                if (acertou) {
                    if (cel.ocupante().estaAfundada()) {
                        resultados.add(ResultadoTiro.AFUNDOU);
                    } else {
                        resultados.add(ResultadoTiro.ACERTO);
                    }
                }
            } else {
                resultados.add(ResultadoTiro.ERRO);
            }
        }
        return resultados;
    }

}
