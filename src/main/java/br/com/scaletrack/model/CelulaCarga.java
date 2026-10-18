package br.com.scaletrack.model;
import java.util.List;
import java.util.ArrayList;

public class CelulaCarga {
    private String numeroSerie;

    public CelulaCarga(String numeroSerie) {
        if (numeroSerie == null || numeroSerie.isBlank()) {
            throw new IllegalArgumentException("Número de Série não pode estar vazio.");}
        this.numeroSerie = numeroSerie;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }
}