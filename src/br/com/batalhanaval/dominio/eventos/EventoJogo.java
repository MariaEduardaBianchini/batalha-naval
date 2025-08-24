package br.com.batalhanaval.dominio.eventos;

import br.com.batalhanaval.dominio.comum.Coordenada;

/** Evento gerado pelo modelo; a visão decide como exibir. */
public record EventoJogo(
        TipoEvento tipo,
        Coordenada coordenada,         // célula afetada (quando aplicável)
        String embarcacaoId,           // id do navio atingido (ou null)
        String embarcacaoNome,         // nome do navio atingido (ou null)
        int turnoDisponivel            // turno a partir do qual pode ser exibido (p/ atraso de radar)
) { }
