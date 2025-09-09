package br.com.batalhanaval.bootstrap;

import br.com.batalhanaval.controlador.ControladorJogo;
import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.jogador.Jogador;
import java.util.Scanner;

public class Principal {
    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Olá, Capitão! Bem-vindo(a) ao Batalha Naval!");
        System.out.println("Escolha a dificuldade do jogo:");
        System.out.println("1) Fácil");
        System.out.println("2) Médio");
        System.out.println("3) Difícil");
        System.out.print("Sua escolha: ");

        int escolha = scanner.nextInt();
        Dificuldade dificuldade;

        switch (escolha) {
            case 1:
                dificuldade = Dificuldade.FACIL;
                break;
            case 2:
                dificuldade = Dificuldade.MEDIO;
                break;
            case 3:
                dificuldade = Dificuldade.DIFICIL;
                break;
            default:
                System.out.println("Opção inválida. Nível Médio será usado como padrão.");
                dificuldade = Dificuldade.MEDIO;
                break;
        }

        System.out.println("Iniciando jogo no modo " + dificuldade.toString() + "...");

        ControladorJogo ctrl = new ControladorJogo(dificuldade);

        // Cria jogadores com tabuleiro e frota posicionados
        Jogador humano = ControladorJogo.novoJogador("Você", dificuldade, 42L);
        Jogador cpu    = ControladorJogo.novoJogador("CPU",  dificuldade, 77L);

        // Inicia o loop de jogo no console (humano x CPU)
        ctrl.iniciarJogoConsole(humano, cpu);

        scanner.close();
    }
}