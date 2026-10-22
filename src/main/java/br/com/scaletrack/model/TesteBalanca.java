
package br.com.scaletrack.model;

import java.time.LocalDate;

public class TesteBalanca {
    private TipoTeste tipo;
    private boolean aprovado;
    private LocalDate data;
    private String observacao;

    // Construtor para registrar um novo teste
    public TesteBalanca(
            TipoTeste tipo,
            boolean aprovado,
            String observacao
    ) {
        this(tipo, aprovado, LocalDate.now(), observacao);
    }

    // Construtor para recuperar um teste do banco de dados
    public TesteBalanca(
            TipoTeste tipo,
            boolean aprovado,
            LocalDate data,
            String observacao
    ) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Tipo não pode estar vazio."
            );
        }

        if (data == null) {
            throw new IllegalArgumentException(
                    "Data não pode estar vazia."
            );
        }

        if (observacao == null || observacao.isBlank()) {
            throw new IllegalArgumentException(
                    "Observação não pode estar vazia."
            );
        }

        this.tipo = tipo;
        this.aprovado = aprovado;
        this.data = data;
        this.observacao = observacao;
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
