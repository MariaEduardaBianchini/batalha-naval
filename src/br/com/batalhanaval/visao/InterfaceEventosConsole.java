package br.com.batalhanaval.visao;

import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.eventos.EventoJogo;
import br.com.batalhanaval.dominio.eventos.ObservadorJogo;
import br.com.batalhanaval.dominio.eventos.TipoEvento;

public class InterfaceEventosConsole implements ObservadorJogo {

    private final String nomeJogadorAtacante;   // só para logs
    private final TabuleiroVisivel mapaOponente;

    public InterfaceEventosConsole(String nomeJogadorAtacante, TabuleiroVisivel mapaOponente) {
        this.nomeJogadorAtacante = nomeJogadorAtacante;
        this.mapaOponente = mapaOponente;
    }

    @Override
    public void notificar(EventoJogo e) {
        if (e.tipo() == TipoEvento.FIM_DE_JOGO) {
            System.out.println("[EVENTO] FIM DE JOGO! Vencedor: " + nomeJogadorAtacante);
            return;
        }

        Coordenada c = e.coordenada();
        switch (e.tipo()) {
            case TIRO_ERRO -> {
                System.out.println("[" + nomeJogadorAtacante + "] ERRO em " + fmt(c));
                mapaOponente.marcarErro(c.linha(), c.coluna());
            }
            case TIRO_ACERTO -> {
                System.out.println("[" + nomeJogadorAtacante + "] ACERTO em " + fmt(c) + det(e));
                mapaOponente.marcarAcerto(c.linha(), c.coluna());
            }
            case TIRO_AFUNDOU -> {
                System.out.println("[" + nomeJogadorAtacante + "] AFUNDOU em " + fmt(c) + det(e));
                mapaOponente.marcarAcerto(c.linha(), c.coluna());
            }
        }
    }

    private String fmt(Coordenada c) { return "(" + c.linha() + "," + c.coluna() + ")"; }

    private String det(EventoJogo e) {
        return e.embarcacaoNome() == null ? "" : " (" + e.embarcacaoNome() + " " + e.embarcacaoId() + ")";
    }
}
