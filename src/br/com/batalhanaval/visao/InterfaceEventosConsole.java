package br.com.batalhanaval.visao;

import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.eventos.EventoJogo;
import br.com.batalhanaval.dominio.eventos.ObservadorJogo;
import br.com.batalhanaval.dominio.eventos.TipoEvento;

public class InterfaceEventosConsole implements ObservadorJogo {

    @Override
    public void notificar(EventoJogo e) {
        if (e.tipo() == TipoEvento.FIM_DE_JOGO) {
            System.out.println("[EVENTO] FIM DE JOGO!");
            return;
        }

        Coordenada c = e.coordenada();
        switch (e.tipo()) {
            case TIRO_ERRO -> System.out.println("[EVENTO] ERRO em " + format(c));
            case TIRO_ACERTO -> System.out.println("[EVENTO] ACERTO em " + format(c)
                    + detalheNavio(e));
            case TIRO_AFUNDOU -> System.out.println("[EVENTO] AFUNDOU em " + format(c)
                    + detalheNavio(e));
        }
    }

    private String detalheNavio(EventoJogo e) {
        if (e.embarcacaoNome() != null) {
            return " (" + e.embarcacaoNome() + " " + e.embarcacaoId() + ")";
        }
        return "";
    }

    private String format(Coordenada c) {
        return "(" + c.linha() + "," + c.coluna() + ")";
    }
}
