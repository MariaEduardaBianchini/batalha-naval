package br.com.batalhanaval.dominio.eventos;

public interface ObservadorJogo {
    void notificar(EventoJogo evento);
}
