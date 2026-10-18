package br.com.scaletrack.model;

import java.time.LocalDate;

public class SubstituicaoCelula {
    private CelulaCarga celulaRemovida;
    private CelulaCarga celulaInstalada;
    private String motivo;
    private LocalDate data;

    public SubstituicaoCelula (CelulaCarga celulaRemovida, CelulaCarga celulaInstalada, String motivo) {
        if (celulaRemovida == null) {
            throw new IllegalArgumentException("Celula não pode estar vazia");
        }
        this.celulaRemovida = celulaRemovida;

        if (celulaInstalada == null) {
            throw new IllegalArgumentException("Celula não pode estar vazia");
        }
        this.celulaInstalada = celulaInstalada;

        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Motivo não pode estar vazio.");
        }
        this.motivo = motivo;
        this.data = LocalDate.now();
    }

    public CelulaCarga getCelulaRemovida() {
        return celulaRemovida;
    }

    public CelulaCarga getCelulaInstalada() {
        return celulaInstalada;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDate getData() {
        return data;
    }
}
