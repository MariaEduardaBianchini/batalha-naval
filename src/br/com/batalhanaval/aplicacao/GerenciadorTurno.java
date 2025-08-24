package br.com.batalhanaval.aplicacao;

import br.com.batalhanaval.dominio.arma.Arma;
import br.com.batalhanaval.dominio.arma.Armas;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.comum.ResultadoTiro;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.eventos.EventoJogo;
import br.com.batalhanaval.dominio.eventos.PublicadorEventos;
import br.com.batalhanaval.dominio.eventos.TipoEvento;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;
import br.com.batalhanaval.infraestrutura.ConfiguracaoJogo;

import java.util.*;

/**
 * Controla turnos e publica eventos, aplicando atraso de radar no modo Difícil.
 */
public class GerenciadorTurno {

    private final Dificuldade dificuldade;
    private final PublicadorEventos publicador;
    private final Queue<EventoJogo> filaAtrasados = new ArrayDeque<>();
    private int turnoAtual = 1; // começa no turno 1

    public GerenciadorTurno(Dificuldade dificuldade, PublicadorEventos publicador) {
        this.dificuldade = dificuldade;
        this.publicador = publicador;
    }

    public int turnoAtual() { return turnoAtual; }

    /**
     * Aplica uma jogada completa com geração de eventos (com ou sem atraso).
     * Retorna a lista de resultados por célula afetada.
     */
    public List<ResultadoTiro> aplicarJogadaComEventos(Embarcacao atacante,
                                                       Arma arma,
                                                       Coordenada alvo,
                                                       Tabuleiro tabuleiroDefensor,
                                                       List<Embarcacao> frotaDefensor) {

        // Calcula coordenadas afetadas (para casar cada Evento com uma célula)
        List<Coordenada> alvos = arma.calcularAlvos(alvo, tabuleiroDefensor);

        // Aplica a jogada validando limites/permite
        List<ResultadoTiro> resultados = MotorRegras.aplicarJogada(atacante, arma, alvo, tabuleiroDefensor);

        // Constrói eventos pareando resultado ↔ coordenada
        int atraso = ConfiguracaoJogo.instancia().atrasoRadarEmTurnos(dificuldade);
        for (int i = 0; i < resultados.size() && i < alvos.size(); i++) {
            ResultadoTiro r = resultados.get(i);
            Coordenada c = alvos.get(i);

            TipoEvento tipo = switch (r) {
                case ERRO -> TipoEvento.TIRO_ERRO;
                case ACERTO -> TipoEvento.TIRO_ACERTO;
                case AFUNDOU -> TipoEvento.TIRO_AFUNDOU;
            };

            // tenta descobrir embarcação atingida (se houver) olhando a célula após o tiro:
            String id = null, nome = null;
            var cel = tabuleiroDefensor.celulaEm(c).orElse(null);
            if (cel != null && cel.temOcupante()) {
                id = cel.ocupante().id();
                nome = cel.ocupante().nome();
            }

            EventoJogo ev = new EventoJogo(
                    tipo,
                    c,
                    id,
                    nome,
                    turnoAtual + atraso
            );

            // atraso: enfileira; sem atraso: publica na hora
            if (atraso > 0) {
                filaAtrasados.add(ev);
            } else {
                publicador.publicar(ev);
            }
        }

        // Se acabou o jogo, publica FIM_DE_JOGO
        if (MotorRegras.vitoria(frotaDefensor)) {
            EventoJogo fim = new EventoJogo(TipoEvento.FIM_DE_JOGO, null, null, null, turnoAtual + atraso);
            if (atraso > 0) filaAtrasados.add(fim); else publicador.publicar(fim);
        }

        return resultados;
    }

    /** Avança 1 turno e libera eventos cujo turnoDisponivel <= turnoAtual. */
    public void avancarTurno() {
        turnoAtual++;
        while (!filaAtrasados.isEmpty() && filaAtrasados.peek().turnoDisponivel() <= turnoAtual) {
            publicador.publicar(filaAtrasados.poll());
        }
    }
}
