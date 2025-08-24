package br.com.batalhanaval.infraestrutura;
import br.com.batalhanaval.dominio.comum.Dificuldade;

/**
 * Singleton com parâmetros do jogo por dificuldade.
 */
public final class ConfiguracaoJogo {

    private static final ConfiguracaoJogo INSTANCIA = new ConfiguracaoJogo();

    private ConfiguracaoJogo() {}

    public static ConfiguracaoJogo instancia() {
        return INSTANCIA;
    }

    /** Dimensão N de um tabuleiro NxN por dificuldade. */
    public int dimensaoQuadrada(Dificuldade dificuldade) {
        return switch (dificuldade) {
            case FACIL -> 10;
            case MEDIO -> 15;
            case DIFICIL -> 30;
        };
    }

    /** Limite de tiros especiais (duplo/explosivo) por embarcação. -1 = ilimitado. */
    public int limiteTirosEspeciaisPorEmbarcacao(Dificuldade dificuldade) {
        return switch (dificuldade) {
            case FACIL -> -1;
            case MEDIO -> 3;
            case DIFICIL -> 1;
        };
    }
    /** Atraso de radar em turnos. */
    public int atrasoRadarEmTurnos(Dificuldade dificuldade) {
        return switch (dificuldade) {
            case FACIL, MEDIO -> 0;
            case DIFICIL -> 3;
        };
    }
}
