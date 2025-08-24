package br.com.batalhanaval.dominio.fabricas;

import br.com.batalhanaval.dominio.comum.Dificuldade;
import br.com.batalhanaval.dominio.embarcacao.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cria a frota correta de acordo com a dificuldade.
 * Nomes/IDs seguem um prefixo por tipo e contador incremental.
 */
public final class FabricaEmbarcacoes {

    private FabricaEmbarcacoes() { }

    public static List<Embarcacao> criarFrota(Dificuldade dificuldade) {
        return switch (dificuldade) {
            case FACIL -> frotaFacil();
            case MEDIO -> frotaMedio();
            case DIFICIL -> frotaDificil();
        };
    }

    private static List<Embarcacao> frotaFacil() {
        List<Embarcacao> lista = new ArrayList<>();
        int pa = 0, cr = 0, fr = 0, de = 0, su = 0;

        lista.add(new PortaAvioes("PA-" + (++pa)));
        lista.add(new Cruzador("CR-" + (++cr)));
        lista.add(new Fragata("FR-" + (++fr)));
        lista.add(new Fragata("FR-" + (++fr)));
        lista.add(new Destroier("DE-" + (++de)));
        lista.add(new Destroier("DE-" + (++de)));
        lista.add(new Submarino("SU-" + (++su)));
        lista.add(new Submarino("SU-" + (++su)));

        return lista;
    }

    private static List<Embarcacao> frotaMedio() {
        List<Embarcacao> lista = new ArrayList<>();
        int pa = 0, cr = 0, fr = 0, de = 0, su = 0;

        lista.add(new PortaAvioes("PA-" + (++pa)));

        lista.add(new Cruzador("CR-" + (++cr)));
        lista.add(new Cruzador("CR-" + (++cr)));

        lista.add(new Fragata("FR-" + (++fr)));
        lista.add(new Fragata("FR-" + (++fr)));
        lista.add(new Fragata("FR-" + (++fr)));

        lista.add(new Destroier("DE-" + (++de)));
        lista.add(new Destroier("DE-" + (++de)));

        lista.add(new Submarino("SU-" + (++su)));
        lista.add(new Submarino("SU-" + (++su)));
        lista.add(new Submarino("SU-" + (++su)));

        return lista;
    }

    private static List<Embarcacao> frotaDificil() {
        List<Embarcacao> lista = new ArrayList<>();
        int pa = 0, cr = 0, fr = 0, de = 0, su = 0;

        lista.add(new PortaAvioes("PA-" + (++pa)));
        lista.add(new PortaAvioes("PA-" + (++pa)));

        lista.add(new Cruzador("CR-" + (++cr)));
        lista.add(new Cruzador("CR-" + (++cr)));
        lista.add(new Cruzador("CR-" + (++cr)));

        lista.add(new Fragata("FR-" + (++fr)));
        lista.add(new Fragata("FR-" + (++fr)));
        lista.add(new Fragata("FR-" + (++fr)));
        lista.add(new Fragata("FR-" + (++fr)));

        lista.add(new Destroier("DE-" + (++de)));
        lista.add(new Destroier("DE-" + (++de)));
        lista.add(new Destroier("DE-" + (++de)));
        lista.add(new Destroier("DE-" + (++de)));

        lista.add(new Submarino("SU-" + (++su)));
        lista.add(new Submarino("SU-" + (++su)));
        lista.add(new Submarino("SU-" + (++su)));
        lista.add(new Submarino("SU-" + (++su)));
        lista.add(new Submarino("SU-" + (++su)));

        return lista;
    }
}
