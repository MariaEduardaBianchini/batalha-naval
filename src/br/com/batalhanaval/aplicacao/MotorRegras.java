package br.com.batalhanaval.aplicacao;

import br.com.batalhanaval.dominio.arma.Arma;
import br.com.batalhanaval.dominio.arma.Armas;
import br.com.batalhanaval.dominio.arma.TipoArma;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.comum.ResultadoTiro;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

/**
 * Orquestra regras de uso de armas e condição de vitória (sem UI).
 */
public final class MotorRegras {
    private MotorRegras() {}

    /** Configura limites de armas em toda a frota conforme a dificuldade. */
    public static void configurarLimitesFrota(List<Embarcacao> frota, Dificuldade dificuldade) {
        for (Embarcacao e : frota) {
            e.configurarLimites(dificuldade);
        }
    }

    /** True se todas as embarcações do defensor estão afundadas. */
    public static boolean vitoria(List<Embarcacao> frotaDefensor) {
        return frotaDefensor.stream().allMatch(Embarcacao::estaAfundada);
    }

    /**
     * Aplica uma jogada completa: valida arma/limites, consome uso e dispara no tabuleiro do defensor.
     * @return lista de resultados (por célula afetada). Chame {@link #vitoria(List)} após para checar fim.
     */
    public static List<ResultadoTiro> aplicarJogada(Embarcacao embarcacaoAtacante,
                                                    Arma arma,
                                                    Coordenada alvo,
                                                    Tabuleiro tabuleiroDefensor) {
        // validação de permissão/limite
        TipoArma tipo = Armas.tipoDe(arma);
        if (!embarcacaoAtacante.permite(tipo)) {
            throw new IllegalStateException(embarcacaoAtacante.nome() + " não permite " + tipo);
        }
        if (embarcacaoAtacante.usosRestantes(tipo) == 0) {
            throw new IllegalStateException(embarcacaoAtacante.nome() + " sem usos para " + tipo);
        }

        // consome uso (se não for ilimitado)
        embarcacaoAtacante.consumirUso(tipo);

        // dispara no tabuleiro do defensor
        return new ArrayList<>(tabuleiroDefensor.aplicarTiro(arma, alvo));
    }
}
