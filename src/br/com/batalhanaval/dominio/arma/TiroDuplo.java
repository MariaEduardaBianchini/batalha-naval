package br.com.batalhanaval.dominio.arma;

import br.com.batalhanaval.dominio.comum.Coordenada;
import br.com.batalhanaval.dominio.tabuleiro.Tabuleiro;

import java.util.ArrayList;
import java.util.List;

/**
 * Afeta duas células adjacentes na mesma linha ou coluna.
 * (Aqui simplificamos: vamos pegar centro + à direita,
 * se não couber, pega centro + à esquerda;
 * depois vamos permitir escolher direção via UI).
 */
public class TiroDuplo implements Arma {

    @Override
    public String nome() {
        return "Tiro Duplo";
    }

    @Override
    public List<Coordenada> calcularAlvos(Coordenada centro, Tabuleiro tabuleiro) {
        List<Coordenada> alvos = new ArrayList<>();
        alvos.add(centro);

        // tenta adicionar vizinho à direita
        Coordenada direita = new Coordenada(centro.linha(), centro.coluna() + 1);
        if (tabuleiro.dentroDosLimites(direita)) {
            alvos.add(direita);
        } else {
            // senão, pega à esquerda
            Coordenada esquerda = new Coordenada(centro.linha(), centro.coluna() - 1);
            if (tabuleiro.dentroDosLimites(esquerda)) {
                alvos.add(esquerda);
            }
        }
        return alvos;
    }
}
