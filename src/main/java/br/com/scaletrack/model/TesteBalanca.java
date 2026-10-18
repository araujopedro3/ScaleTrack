package br.com.scaletrack.model;

import java.time.LocalDate;

public class TesteBalanca {
    private TipoTeste tipo;
    private boolean aprovado;
    private LocalDate data;
    private String observacao;

    public TesteBalanca(
            TipoTeste tipo,
            boolean aprovado,
            String observacao
    ) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo não pode estar vazio.");
        }

        this.tipo = tipo;
        this.aprovado = aprovado;

        if (observacao == null || observacao.isBlank()) {
            throw new IllegalArgumentException("Observação não pode estar vazia.");
        }

        this.observacao = observacao;

        this.data = LocalDate.now();
    }

    public TipoTeste getTipo() {
        return tipo;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public LocalDate getData() {
        return data;
    }

    public String getObservacao() {
        return observacao;
    }
}
