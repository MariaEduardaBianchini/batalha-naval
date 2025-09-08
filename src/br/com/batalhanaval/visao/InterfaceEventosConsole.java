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
            System.out.println("-------------------------------------");
            System.out.println("           FIM DE JOGO!              ");
            System.out.println("Parabéns, " + nomeJogadorAtacante + "! Você afundou todos os navios inimigos!");
            System.out.println("-------------------------------------");
            return;
        }

        Coordenada c = e.coordenada();
        switch (e.tipo()) {
            case TIRO_ERRO -> {
                System.out.println("Que pena! Seu tiro na coordenada " + fmt(c) + " atingiu apenas a água.");
                mapaOponente.marcarErro(c.linha(), c.coluna());
            }
            case TIRO_ACERTO -> {
                System.out.println("Excelente! Um tiro certeiro na coordenada " + fmt(c) + "! " + det(e));
                mapaOponente.marcarAcerto(c.linha(), c.coluna());
            }
            case TIRO_AFUNDOU -> {
                System.out.println("BOOM! Embarcação afundada na coordenada " + fmt(c) + "! " + det(e));
                mapaOponente.marcarAcerto(c.linha(), c.coluna());
            }
        }
    }

    private String fmt(Coordenada c) { return "(" + c.linha() + "," + c.coluna() + ")"; }

    private String det(EventoJogo e) {
        return e.embarcacaoNome() == null ? "" : " (" + e.embarcacaoNome() + " " + e.embarcacaoId() + ")";
    }
}
