package br.com.batalhanaval.bootstrap;

import br.com.batalhanaval.aplicacao.GerenciadorTurno;
import br.com.batalhanaval.aplicacao.MotorRegras;
import br.com.batalhanaval.dominio.arma.*;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.eventos.PublicadorEventos;
import br.com.batalhanaval.dominio.fabricas.FabricaEmbarcacoes;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;
import br.com.batalhanaval.visao.InterfaceEventosConsole;

import java.util.List;

public class Principal {
    public static void main(String[] args) {
        // DIFICIL => atraso de 3 turnos; MEDIO/FACIL => sem atraso
        Dificuldade dificuldade = Dificuldade.DIFICIL;

        Tabuleiro tabDefensor = Tabuleiro.criarPorDificuldade(dificuldade);
        List<Embarcacao> frotaDefensor = FabricaEmbarcacoes.criarFrota(dificuldade);
        tabDefensor.posicionarAleatorio(frotaDefensor, 0L);

        // Limites (afeta o atacante nesta demo)
        MotorRegras.configurarLimitesFrota(frotaDefensor, dificuldade);

        PublicadorEventos pub = new PublicadorEventos();
        pub.registrar(new InterfaceEventosConsole());

        GerenciadorTurno turnos = new GerenciadorTurno(dificuldade, pub);

        Embarcacao atacante = frotaDefensor.get(0); // só para demo

        // armas
        Arma explosivo = new TiroExplosivo();
        Arma simples   = new TiroSimples();

        // Turno 1: EXPLOSIVO (consome o único uso no Difícil)
        System.out.println("\n== Turno " + turnos.turnoAtual() + " ==");
        turnos.aplicarJogadaComEventos(atacante, explosivo, new Coordenada(0,0), tabDefensor, frotaDefensor);
        turnos.avancarTurno();

        // Turnos 2,3,4: SIMPLES (ilimitado)
        for (int i = 0; i < 3; i++) {
            System.out.println("\n== Turno " + turnos.turnoAtual() + " ==");
            turnos.aplicarJogadaComEventos(atacante, simples, new Coordenada(0,0), tabDefensor, frotaDefensor);
            turnos.avancarTurno();
        }

        System.out.println("\n(Tabuleiro real do defensor - DEBUG)");
        System.out.println(tabDefensor.desenharComoTexto());
    }
}
