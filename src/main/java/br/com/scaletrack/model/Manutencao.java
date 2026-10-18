package br.com.scaletrack.model;
import java.time.LocalDate;

public class Manutencao {
    private TipoManutencao tipo;
    private String descricao;
    private LocalDate data;

    public Manutencao(TipoManutencao tipo, String descricao) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo não pode estar vazio");
        }
        this.tipo = tipo;
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição nao pode estar vazia, Informe o motivo");
        }
        this.descricao = descricao;
        this.data = LocalDate.now();
    }

    public TipoManutencao getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getData() {
        return data;
    }
}
