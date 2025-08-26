package br.com.batalhanaval.bootstrap;

import br.com.batalhanaval.controlador.ControladorJogo;
import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.jogador.Jogador;

public class Principal {
    public static void main(String[] args) {
        Dificuldade dificuldade = Dificuldade.MEDIO; // FACIL / MEDIO / DIFICIL
        ControladorJogo ctrl = new ControladorJogo(dificuldade);

        // Cria jogadores com tabuleiro e frota posicionados
        Jogador humano = ControladorJogo.novoJogador("Você", dificuldade, 42L);
        Jogador cpu    = ControladorJogo.novoJogador("CPU",  dificuldade, 77L);

        // Inicia o loop de jogo no console (humano x CPU)
        ctrl.iniciarJogoConsole(humano, cpu);
    }
}
