package br.com.scaletrack.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
//import br.com.scaletrack.model.StatusBalanca;

public class Balanca {

    // =========================================================
    // ATRIBUTOS DA BALANÇA
    // =========================================================

    private String numeroSerie;
    private String ordemFabricacao;
    private String modelo;
    private int capacidade;
    private double dimensaoComprimento;
    private double dimensaoLargura;
    private int quantidadeCelulas;
    private StatusBalanca status;


    // =========================================================
    // LISTAS / HISTÓRICOS
    // =========================================================

    // Células que estão atualmente instaladas na balança
    private List<CelulaCarga> celulas;

    // Histórico de todas as substituições de células
    private List<SubstituicaoCelula> historicoSubstituicoes;

    // Histórico de manutenções realizadas na balança
    private List<Manutencao> historicoManutencoes;

    // Hístorico de Testes
    private List<TesteBalanca> historicoTestes;


    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public Balanca(
            String numeroSerie,
            String ordemFabricacao,
            String modelo,
            int capacidade,
            double dimensaoComprimento,
            double dimensaoLargura,
            int quantidadeCelulas) {

        // Validação do número de série
        if (numeroSerie == null || numeroSerie.isBlank()) {
            throw new IllegalArgumentException(
                    "*** Número de Série vazio"
            );
        }
        this.numeroSerie = numeroSerie;


        // Validação da Ordem de Fabricação
        if (ordemFabricacao == null || ordemFabricacao.isBlank()) {
            throw new IllegalArgumentException(
                    "*** OF não pode estar vazia"
            );
        }
        this.ordemFabricacao = ordemFabricacao;


        // Validação do modelo da balança
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException(
                    "*** Modelo não pode estar vazio"
            );
        }
        this.modelo = modelo;


        // Validação da capacidade
        if (capacidade <= 0) {
            throw new IllegalArgumentException(
                    "*** Capacidade deve ser maior que zero"
            );
        }
        this.capacidade = capacidade;


        // Validação do comprimento
        if (dimensaoComprimento <= 0 || dimensaoComprimento > 40) {
            throw new IllegalArgumentException(
                    "*** Comprimento deve ser maior que 0 e menor ou igual a 40"
            );
        }
        this.dimensaoComprimento = dimensaoComprimento;


        // Validação da largura
        if (dimensaoLargura <= 0) {
            throw new IllegalArgumentException(
                    "*** Largura deve ser maior que zero"
            );
        }
        this.dimensaoLargura = dimensaoLargura;


        // Validação da quantidade de células
        if (quantidadeCelulas <= 0) {
            throw new IllegalArgumentException(
                    "*** Quantidade de células deve ser maior que zero"
            );
        }
        this.quantidadeCelulas = quantidadeCelulas;


        // =====================================================
        // REGRA: QUANTIDADE DE CÉLULAS PELO COMPRIMENTO
        // =====================================================

        if (dimensaoComprimento <= 23 && quantidadeCelulas != 8) {

            throw new IllegalArgumentException(
                    "*** Quantidade de células deve ser 8"
            );

        } else if (dimensaoComprimento > 23
                && dimensaoComprimento <= 28
                && quantidadeCelulas != 10) {

            throw new IllegalArgumentException(
                    "*** Quantidade de células deve ser 10"
            );

        } else if (dimensaoComprimento > 28
                && dimensaoComprimento <= 33
                && quantidadeCelulas != 12) {

            throw new IllegalArgumentException(
                    "*** Quantidade de células deve ser 12"
            );

        } else if (dimensaoComprimento > 33
                && dimensaoComprimento <= 36
                && quantidadeCelulas != 14) {

            throw new IllegalArgumentException(
                    "*** Quantidade de células deve ser 14"
            );

        } else if (dimensaoComprimento > 36
                && dimensaoComprimento <= 40
                && quantidadeCelulas != 16) {

            throw new IllegalArgumentException(
                    "*** Quantidade de células deve ser 16"
            );
        }


        // =====================================================
        // INICIALIZAÇÃO DAS LISTAS
        // =====================================================

        this.celulas = new ArrayList<>();
        this.historicoSubstituicoes = new ArrayList<>();
        this.historicoManutencoes = new ArrayList<>();
        this.status = StatusBalanca.EM_FABRICACAO;
        this.historicoTestes = new ArrayList<>();
    }

    public Balanca(
            String numeroSerie,
            String ordemFabricacao,
            String modelo,
            int capacidade,
            double dimensaoComprimento,
            double dimensaoLargura,
            int quantidadeCelulas,
            StatusBalanca status
    ) {
        this(
                numeroSerie,
                ordemFabricacao,
                modelo,
                capacidade,
                dimensaoComprimento,
                dimensaoLargura,
                quantidadeCelulas
        );

        this.status = java.util.Objects.requireNonNull(
                status, "Status não pode ser nulo"
        );
    }


    // =========================================================
    // CÉLULAS DE CARGA
    // =========================================================

    /**
     * Adiciona uma célula de carga à balança.
     * Impede ultrapassar o limite de células e NS duplicado.
     */
    public void adicionarCelula(CelulaCarga celula) {

        // Impede adicionar uma célula inexistente
        if(celula == null) {
            throw new IllegalArgumentException("Célula não pode estar vazia.");
        }

        // Verifica se a balança já atingiu o limite de células
        if (celulas.size() >= quantidadeCelulas) {
            throw new IllegalArgumentException(
                    "Limite de células atingido"
            );
        }

        // Verifica se já existe uma célula com o mesmo NS
        for (CelulaCarga celulaInstalada : celulas) {

            if (celulaInstalada.getNumeroSerie()
                    .equals(celula.getNumeroSerie())) {

                throw new IllegalArgumentException(
                        "Número de Série já requisitado."
                );
            }
        }

        // Adiciona a célula à lista
        celulas.add(celula);
    }


    /**
     * Remove uma célula da balança através do número de série.
     */
    public void removerCelula(String numeroSerie) {

        Iterator<CelulaCarga> iterator = celulas.iterator();

        while (iterator.hasNext()) {

            CelulaCarga celula = iterator.next();

            if (celula.getNumeroSerie().equals(numeroSerie)) {

                iterator.remove();
                return;
            }
        }
    }


    /**
     * Substitui uma célula instalada por uma nova
     * e registra a troca no histórico.
     */
    public void substituirCelula(
            String numeroSerieRemovido,
            CelulaCarga novaCelula,
            String motivo) {

        // Valida o número de série informado
        if (numeroSerieRemovido == null || numeroSerieRemovido.isBlank()) {
            throw new IllegalArgumentException(
                    "Número de série não pode estar vazio.");
        }

        if (novaCelula == null) {
            throw new IllegalArgumentException("Nova Célula não pode estar vazia.");
        }

        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Motivo da substituição não pode estar vazio.");
        }

        for (CelulaCarga celulaInstalada : celulas) {

            if (celulaInstalada.getNumeroSerie() .equals(novaCelula.getNumeroSerie())) {
                throw new IllegalArgumentException("Número de Série precisa ser diferente do Número de Série ja instalado!");
            }
        }
        CelulaCarga celulaRemovida = null;

        // Procura a célula que será substituída
        for (CelulaCarga celula : celulas) {

            if (celula.getNumeroSerie()
                    .equals(numeroSerieRemovido)) {

                celulaRemovida = celula;
            }
        }


        // Verifica se a célula realmente existe na balança
        if (celulaRemovida == null) {

            throw new IllegalArgumentException(
                    "Célula não encontrada na balança."
            );
        }


        // Remove a célula antiga
        removerCelula(numeroSerieRemovido);

        // Instala a nova célula
        adicionarCelula(novaCelula);


        // Cria o registro da substituição
        SubstituicaoCelula substituicao =
                new SubstituicaoCelula(
                        celulaRemovida,
                        novaCelula,
                        motivo
                );


        // Salva a substituição no histórico
        historicoSubstituicoes.add(substituicao);
    }


    // =========================================================
    // MANUTENÇÕES
    // =========================================================

    /**
     * Registra uma manutenção realizada na balança.
     */
    public void adicionarManutencao(Manutencao manutencao) {

        // Impede adicionar uma manutenção inexistente
        if (manutencao == null) {

            throw new IllegalArgumentException(
                    "Manutenção não pode estar vazia!"
            );
        }

        if (status != StatusBalanca.EM_MANUTENCAO) {
            throw new IllegalArgumentException("A balança precisa estar na fase MANUTENÇÃO para registrar uma manutenção");
        }

        // Adiciona a manutenção ao histórico
        historicoManutencoes.add(manutencao);
    }

    public void iniciarTestes() {
        if (status != StatusBalanca.EM_FABRICACAO) {
            throw new IllegalArgumentException("Balança precisa estar em Fabricação para iniciar a fase de Testes!");
        }

        this.status = StatusBalanca.EM_TESTE;
    }


    public void liberarBalanca() {
        if (status != StatusBalanca.EM_TESTE) {
            throw new IllegalArgumentException(
                    "Balança precisa estar em Teste para ser Liberada!"
            );
        }

        if (historicoTestes.isEmpty()) {
            throw new IllegalArgumentException(
                    "A balança precisa de testes para ser Liberada."
            );
        }

        boolean testeCarga = false;
        boolean testeRepetibilidade = false;
        boolean testeExcentricidade = false;
        boolean testeCelulas = false;

        LocalDate hoje = LocalDate.now();

        for (TesteBalanca teste : historicoTestes) {
            boolean dentroDoPrazo =
                    !teste.getData().plusMonths(3).isBefore(hoje);

            boolean aprovadoEValido =
                    teste.isAprovado() && dentroDoPrazo;

            switch (teste.getTipo()) {
                case CARGA:
                    testeCarga = aprovadoEValido;
                    break;

                case REPETIBILIDADE:
                    testeRepetibilidade = aprovadoEValido;
                    break;

                case EXCENTRICIDADE:
                    testeExcentricidade = aprovadoEValido;
                    break;

                case VERIFICACAO_CELULAS:
                    testeCelulas = aprovadoEValido;
                    break;
            }
        }

        if (!testeCarga || !testeRepetibilidade
                || !testeExcentricidade || !testeCelulas) {
            throw new IllegalArgumentException(
                    "Todos os testes obrigatórios precisam estar aprovados "
                            + "e dentro do prazo de 3 meses para liberar a balança."
            );
        }

        this.status = StatusBalanca.LIBERADA;
    }


    public void iniciarManutencao() {
        if (status != StatusBalanca.LIBERADA) {
            throw new IllegalArgumentException("Apenas Balanças liberadas podem entrar na fse MANUTENÇÃO!");
        }

        this.status = StatusBalanca.EM_MANUTENCAO;
    }

    public void finalizarManutencao() {
        if (status != StatusBalanca.EM_MANUTENCAO) {
            throw new IllegalArgumentException("Apenas Balanças que estão na fase MANUTENÇÃO podem ser Liberadas novamente!");
        }

        this.status = StatusBalanca.LIBERADA;
    }

    // =========================================================
    // TESTES DA BALANÇA
    // =========================================================

    public void registrarTeste(TesteBalanca teste) {
        if(teste == null) {
            throw new IllegalArgumentException("Teste não pode estar vázio");
        }

        if(status != StatusBalanca.EM_TESTE) {
            throw new IllegalArgumentException("A balança precisa estar em fase de TESTE");
        }

        for (TesteBalanca testeRegistrado :  historicoTestes) {

            if (testeRegistrado.getTipo() == teste.getTipo() && testeRegistrado.isAprovado()) {
                throw new IllegalArgumentException("Este tipo de teste já foi registrado.");
            }
        }

        historicoTestes.add(teste);
    }

    // =========================================================
    // GETTERS - DADOS DA BALANÇA
    // =========================================================

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public String getOrdemFabricacao() {
        return ordemFabricacao;
    }

    public String getModelo() {
        return modelo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public double getDimensaoComprimento() {
        return dimensaoComprimento;
    }

    public double getDimensaoLargura() {
        return dimensaoLargura;
    }

    public int getQuantidadeCelulas() {
        return quantidadeCelulas;
    }

    public StatusBalanca getStatus() {
        return status;
    }


    // =========================================================
    // GETTERS - CÉLULAS
    // =========================================================

    public int getQuantidadeCelulasInstaladas() {
        return celulas.size();
    }

    public List<CelulaCarga> getCelulas() {
        return Collections.unmodifiableList(celulas);
    }


    // =========================================================
    // GETTERS - HISTÓRICOS
    // =========================================================

    public List<SubstituicaoCelula> getHistoricoSubstituicoes() {
        return Collections.unmodifiableList(historicoSubstituicoes);
    }

    public List<Manutencao> getHistoricoManutencoes() {
        return Collections.unmodifiableList(historicoManutencoes);
    }

    public List<TesteBalanca> getHistoricoTestes() {
        return Collections.unmodifiableList(historicoTestes);
    }
}