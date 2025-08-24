package br.com.batalhanaval.dominio.embarcacao;

import br.com.batalhanaval.dominio.arma.TipoArma;
import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.comum.Dificuldade;

import java.util.*;

/**
 * Classe base para qualquer embarcação.
 */
public abstract class Embarcacao {

    private final String id;
    private final String nome;
    private final int tamanho;

    private final List<Coordenada> posicoes = new ArrayList<>();
    private final Set<Coordenada> acertos = new HashSet<>();

    // >>> NOVO: permissões e limites de armas <<<
    private final EnumSet<TipoArma> armasPermitidas = EnumSet.noneOf(TipoArma.class);
    private final EnumMap<TipoArma, Integer> usosRestantes = new EnumMap<>(TipoArma.class); // -1 = ilimitado

    protected Embarcacao(String id, String nome, int tamanho) {
        if (tamanho <= 0) throw new IllegalArgumentException("Tamanho inválido.");
        this.id = id;
        this.nome = nome;
        this.tamanho = tamanho;
    }

    public String id() { return id; }
    public String nome() { return nome; }
    public int tamanho() { return tamanho; }

    public List<Coordenada> posicoes() { return List.copyOf(posicoes); }

    public void definirPosicoes(List<Coordenada> novasPosicoes) {
        if (novasPosicoes == null || novasPosicoes.size() != tamanho) {
            throw new IllegalArgumentException("Quantidade de posições diferente do tamanho da embarcação.");
        }
        posicoes.clear();
        posicoes.addAll(novasPosicoes);
        acertos.clear();
    }

    public boolean registrarAcerto(Coordenada c) {
        if (ocupa(c)) {
            acertos.add(c);
            return true;
        }
        return false;
    }

    public boolean ocupa(Coordenada c) { return posicoes.contains(c); }

    public boolean estaAfundada() { return acertos.size() == tamanho; }

    public int acertos() { return acertos.size(); }

    // ======= Armas/limites =======
    public void configurarLimites(Dificuldade dificuldade) {
        armasPermitidas.clear();
        usosRestantes.clear();

        // Permissões por classe
        if (this instanceof PortaAvioes) {
            armasPermitidas.addAll(Arrays.asList(TipoArma.SIMPLES, TipoArma.DUPLO, TipoArma.EXPLOSIVO));
        } else if (this instanceof Cruzador || this instanceof Fragata) {
            armasPermitidas.addAll(Arrays.asList(TipoArma.SIMPLES, TipoArma.DUPLO));
        } else if (this instanceof Destroier || this instanceof Submarino) {
            armasPermitidas.add(TipoArma.SIMPLES);
        }

        // Limites por dificuldade:
        // - SIMPLES: sempre ilimitado (-1)
        // - DUPLO/EXPLOSIVO: FACIL = ilimitado (-1); MEDIO = 3; DIFICIL = 1 (se a arma for permitida)
        usosRestantes.put(TipoArma.SIMPLES, -1);
        if (armasPermitidas.contains(TipoArma.DUPLO)) {
            usosRestantes.put(TipoArma.DUPLO,
                    dificuldade == Dificuldade.FACIL ? -1 : (dificuldade == Dificuldade.MEDIO ? 3 : 1));
        }
        if (armasPermitidas.contains(TipoArma.EXPLOSIVO)) {
            usosRestantes.put(TipoArma.EXPLOSIVO,
                    dificuldade == Dificuldade.FACIL ? -1 : (dificuldade == Dificuldade.MEDIO ? 3 : 1));
        }
    }

    public boolean permite(TipoArma tipo) { return armasPermitidas.contains(tipo); }

    /** -1 = ilimitado; se não permitir, retorna 0. */
    public int usosRestantes(TipoArma tipo) {
        if (!permite(tipo)) return 0;
        return usosRestantes.getOrDefault(tipo, 0);
    }

    /** Consome 1 uso (se não for ilimitado). Lança se não permitido ou sem usos. */
    public void consumirUso(TipoArma tipo) {
        if (!permite(tipo)) {
            throw new IllegalStateException(nome + " não permite " + tipo);
        }
        int atual = usosRestantes.getOrDefault(tipo, 0);
        if (atual == 0) {
            throw new IllegalStateException(nome + " sem usos restantes para " + tipo);
        }
        if (atual > 0) {
            usosRestantes.put(tipo, atual - 1);
        }
        // se -1, não altera (ilimitado)
    }

    @Override
    public String toString() {
        return nome + "{" + id + ", tam=" + tamanho + ", acertos=" + acertos.size() + "/" + tamanho + "}";
    }
}
