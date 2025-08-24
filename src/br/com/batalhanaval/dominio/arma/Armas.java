package br.com.batalhanaval.dominio.arma;

public final class Armas {
    private Armas() {}

    public static TipoArma tipoDe(Object arma) {
        if (arma instanceof TiroSimples) {
            return TipoArma.SIMPLES;
        } else if (arma instanceof TiroDuplo) {
            return TipoArma.DUPLO;
        } else if (arma instanceof TiroExplosivo) {
            return TipoArma.EXPLOSIVO;
        } else {
            throw new IllegalArgumentException("Arma desconhecida: " + arma);
        }
    }
}
