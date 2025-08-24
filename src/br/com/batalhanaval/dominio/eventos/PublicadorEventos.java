package br.com.batalhanaval.dominio.eventos;

import java.util.ArrayList;
import java.util.List;

public class PublicadorEventos {
    private final List<ObservadorJogo> observadores = new ArrayList<>();

    public void registrar(ObservadorJogo obs) {
        observadores.add(obs);
    }
    public void remover(ObservadorJogo obs) {
        observadores.remove(obs);
    }

    public void publicar(EventoJogo evento) {
        for (var obs : observadores) {
            obs.notificar(evento);
        }
    }
}
