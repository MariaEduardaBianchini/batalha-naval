package br.com.batalhanaval.controlador;

import br.com.batalhanaval.aplicacao.GerenciadorTurno;
import br.com.batalhanaval.aplicacao.MotorRegras;
import br.com.batalhanaval.dominio.arma.*;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.embarcacao.Embarcacao;
import br.com.batalhanaval.dominio.eventos.PublicadorEventos;
import br.com.batalhanaval.dominio.fabricas.FabricaEmbarcacoes;
import br.com.batalhanaval.dominio.jogador.Jogador;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;
import br.com.batalhanaval.visao.InterfaceEventosConsole;
import br.com.batalhanaval.visao.TabuleiroVisivel;
import br.com.batalhanaval.aplicacao.ia.EstrategiaIA;
import br.com.batalhanaval.aplicacao.ia.IARandom;
// import br.com.batalhanaval.aplicacao.ia.IAHuntTarget; // se quiser testar a outra


import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ControladorJogo {

    private final Dificuldade dificuldade;

    public ControladorJogo(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public void iniciarJogoConsole(Jogador humano, Jogador computador) {
        Scanner in = new Scanner(System.in);

        // Tabuleiro "visível" do oponente (para o humano)
        TabuleiroVisivel visHumanoSobreCPU =
                new TabuleiroVisivel(computador.tabuleiro().linhas(), computador.tabuleiro().colunas());

        // Eventos
        PublicadorEventos pub = new PublicadorEventos();
        pub.registrar(new InterfaceEventosConsole(humano.nome(), visHumanoSobreCPU));

        GerenciadorTurno turnos = new GerenciadorTurno(dificuldade, pub);

        EstrategiaIA ia = new IARandom(); // troque por new IAHuntTarget() quando quiser

        // Limites por dificuldade
        MotorRegras.configurarLimitesFrota(humano.frota(), dificuldade);
        MotorRegras.configurarLimitesFrota(computador.frota(), dificuldade);

        // Loop
        while (true) {
            System.out.println("\n>>> Atenção! É a sua vez, Capitão! TURNO " + turnos.turnoAtual() + " <<<");
            System.out.println("Boa sorte :)");

            // HUMANO ataca
            Embarcacao embarcacao = escolherEmbarcacao(in, humano);
            Arma arma = escolherArma(in, embarcacao);
            Coordenada alvo = lerCoordenada(in, computador.tabuleiro());

            turnos.aplicarJogadaComEventos(embarcacao, arma, alvo, computador.tabuleiro(), computador.frota());
            if (MotorRegras.vitoria(computador.frota())) {
                turnos.avancarTurno(); // libera evento de FIM no modo Difícil
                break;
            }

            // CPU ataca (placeholder: tiro simples em célula pseudo-aleatória)
            var escolha = ia.decidir(computador, humano);
            System.out.println("\nO inimigo se prepara para o ataque! Aguarde por favor...");
            turnos.aplicarJogadaComEventos(escolha.atacante(), escolha.arma(), escolha.alvo(),
                    humano.tabuleiro(), humano.frota());

            turnos.avancarTurno();

            // Renderizações
            System.out.println("\n--- Seu tabuleiro (real) ---");
            System.out.println(humano.tabuleiro().desenharComoTexto());
            System.out.println("--- Oponente (visível) ---");
            System.out.println(visHumanoSobreCPU.desenharComoTexto());

            if (MotorRegras.vitoria(humano.frota())) {
                break;
            }
        }
        in.close();
    }

    private Embarcacao escolherEmbarcacao(Scanner in, Jogador j) {
        System.out.println("\nEscolha uma de suas embarcações para atacar o inimigo:");
        List<Embarcacao> vivas = j.frota().stream()
                .filter(e -> !e.estaAfundada())
                .sorted(Comparator.comparing(Embarcacao::nome))
                .toList();
        IntStream.range(0, vivas.size()).forEach(i ->
                System.out.println((i+1) + ") " + vivas.get(i) + "  [S:" + vivas.get(i).usosRestantes(TipoArma.SIMPLES)
                        + ", D:" + vivas.get(i).usosRestantes(TipoArma.DUPLO)
                        + ", E:" + vivas.get(i).usosRestantes(TipoArma.EXPLOSIVO) + "]"));
        int opc = lerInt(in, 1, vivas.size());
        return vivas.get(opc - 1);
    }

    private Arma escolherArma(Scanner in, Embarcacao e) {
        System.out.println("Escolha arma:");
        System.out.println("1) Tiro Simples"   + (e.usosRestantes(TipoArma.SIMPLES)   == 0 ? " (indisponível)" : ""));
        System.out.println("2) Tiro Duplo"     + (e.usosRestantes(TipoArma.DUPLO)     == 0 ? " (indisponível)" : ""));
        System.out.println("3) Tiro Explosivo" + (e.usosRestantes(TipoArma.EXPLOSIVO) == 0 ? " (indisponível)" : ""));
        while (true) {
            int opc = lerInt(in, 1, 3);
            if (opc == 1 && e.usosRestantes(TipoArma.SIMPLES)   != 0) return new TiroSimples();
            if (opc == 2 && e.usosRestantes(TipoArma.DUPLO)     != 0) return new TiroDuplo();
            if (opc == 3 && e.usosRestantes(TipoArma.EXPLOSIVO) != 0) return new TiroExplosivo();
            System.out.println("Essa arma não tem mais munição, Capitão. Escolha outra para o seu ataque:");
        }
    }

    private Coordenada lerCoordenada(Scanner in, Tabuleiro alvo) {
        System.out.print("Linha (0-" + (alvo.linhas()-1) + "): ");
        int l = lerInt(in, 0, alvo.linhas()-1);
        System.out.print("Coluna (0-" + (alvo.colunas()-1) + "): ");
        int c = lerInt(in, 0, alvo.colunas()-1);
        return new Coordenada(l, c);
    }

    private int lerInt(Scanner in, int min, int max) {
        while (true) {
            String s = in.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.print("Coordenada inválida, Capitão. Por favor, digite um valor entre " + min + " e " + max + ": ");
        }
    }

    // IA placeholder (Passo 10 melhora)
    private Coordenada escolherAlvoCPU(Tabuleiro tabDoHumano, int turno) {
        int l = (turno * 3) % tabDoHumano.linhas();
        int c = (turno * 5) % tabDoHumano.colunas();
        return new Coordenada(l, c);
    }

    private Embarcacao primeiroAtacanteDisponivel(List<Embarcacao> frota) {
        return frota.stream().filter(e -> !e.estaAfundada()).findFirst().orElse(frota.get(0));
    }

    // Helper p/ criar jogadores com tabuleiro e frota prontos
    public static Jogador novoJogador(String nome, Dificuldade dificuldade, long seedPosicionamento) {
        Tabuleiro t = Tabuleiro.criarPorDificuldade(dificuldade);
        List<Embarcacao> frota = FabricaEmbarcacoes.criarFrota(dificuldade);
        t.posicionarAleatorio(frota, seedPosicionamento);
        return new Jogador(nome, t, frota);
    }
}
