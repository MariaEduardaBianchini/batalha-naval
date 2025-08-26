package br.com.batalhanaval.visao;

public class TabuleiroVisivel {
    private final EstadoVisivel[][] mapa;

    public TabuleiroVisivel(int linhas, int colunas) {
        this.mapa = new EstadoVisivel[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                mapa[i][j] = EstadoVisivel.DESCONHECIDO;
            }
        }
    }

    public void marcarErro(int l, int c)   { mapa[l][c] = EstadoVisivel.ERRO; }
    public void marcarAcerto(int l, int c) { mapa[l][c] = EstadoVisivel.ACERTO; }

    public int linhas()  { return mapa.length; }
    public int colunas() { return mapa[0].length; }

    public String desenharComoTexto() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linhas(); i++) {
            for (int j = 0; j < colunas(); j++) {
                char ch = switch (mapa[i][j]) {
                    case DESCONHECIDO -> '~'; // água desconhecida
                    case ERRO        -> 'o';  // água já atirada
                    case ACERTO      -> 'x';  // parte de navio acertada
                };
                sb.append(ch).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
