
package org.example;

import br.com.scaletrack.database.Conexao;
import br.com.scaletrack.model.Balanca;
import br.com.scaletrack.model.CelulaCarga;
import br.com.scaletrack.model.Manutencao;
import br.com.scaletrack.model.SubstituicaoCelula;
import br.com.scaletrack.model.TesteBalanca;
import br.com.scaletrack.model.TipoManutencao;
import br.com.scaletrack.model.TipoTeste;
import br.com.scaletrack.repository.BalancaRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        // =========================================================
        // CONEXÃO COM O BANCO DE DADOS
        // =========================================================

        try (Connection conexao = Conexao.conectar()) {
            System.out.println("Conectado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados!");
            System.out.println(e.getMessage());
            return;
        }

        BalancaRepository repository = new BalancaRepository();

        // =========================================================
        // CADASTRO DE CÉLULA NO BANCO DE DADOS
        // =========================================================

        try {
            String numeroSerieTeste = "CL-TESTE-002";

            CelulaCarga celulaBanco =
                    new CelulaCarga(numeroSerieTeste);

            CelulaCarga celulaExistente =
                    repository.buscarCelulaPorNumeroSerie(numeroSerieTeste);

            if (celulaExistente == null) {
                repository.salvarCelula(celulaBanco, "BAL-0010");
                System.out.println(
                        "Célula de carga cadastrada com sucesso!"
                );
            } else {
                System.out.println(
                        "A célula já está cadastrada no banco de dados."
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar célula de carga!");
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("\n--- Celulas CADASTRADAS NO TESTE---");

            var celulas = repository.listarCelulasPorBalanca("BAL-0010");

            if (celulas.isEmpty()) {
                System.out.println("Nenhum celula encontrada na Balança");
            } else {
                for (CelulaCarga celula : celulas) {
                    System.out.println("Número de Série: " + celula.getNumeroSerie());
                }

                System.out.println("Total de Células: " + celulas.size());
            }
        }
        catch (SQLException e) {
            System.out.println("Erro ao consultar as células");
            System.out.println(e.getMessage());
        }


        try {
            System.out.println("\n--- Balança carregada do banco ---");

            Balanca balancaComCelulas =
                    repository.buscarComCelulas("BAL-0010");

            if (balancaComCelulas == null) {
                System.out.println("Balança não encontrada.");
            } else {
                System.out.println(
                        "Balança: " + balancaComCelulas.getNumeroSerie()
                );

                System.out.println(
                        "Células instaladas: "
                                + balancaComCelulas.getQuantidadeCelulasInstaladas()
                );

                for (CelulaCarga celula : balancaComCelulas.getCelulas()) {
                    System.out.println(
                            "Célula: " + celula.getNumeroSerie()
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao carregar balança com células!");
            System.out.println(e.getMessage());
        }


        // =========================================================
        // CRIAÇÃO DAS BALANÇAS PARA OS TESTES
        // =========================================================

        Balanca balanca1 = new Balanca(
                "BAL-0010",
                "OF-2026-001",
                "Rodoviária",
                120000,
                30,
                3.20,
                12
        );

        Balanca balanca2 = new Balanca(
                "BAL-0011",
                "OF-2026-002",
                "Rodoviária",
                120000,
                30,
                3.20,
                12
        );

        // =========================================================
        // LISTAGEM DAS BALANÇAS ATIVAS
        // =========================================================

        try {
            System.out.println("\n--- Balanças Ativas ---");

            for (Balanca balanca : repository.listarTodas()) {
                System.out.println(
                        "Número de série: " + balanca.getNumeroSerie()
                );

                System.out.println(
                        "Modelo: " + balanca.getModelo()
                );

                System.out.println(
                        "Status: " + balanca.getStatus()
                );

                System.out.println("-----------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar balanças!");
            System.out.println(e.getMessage());
        }

        // =========================================================
        // BUSCA DE UMA BALANÇA PELO NÚMERO DE SÉRIE
        // =========================================================

        try {
            Balanca encontrada =
                    repository.buscarPorNumeroSerie("BAL-0011");

            if (encontrada != null) {
                System.out.println("\nBalança encontrada!");

                System.out.println(
                        "Número de série: " + encontrada.getNumeroSerie()
                );

                System.out.println(
                        "Modelo: " + encontrada.getModelo()
                );

                System.out.println(
                        "Status: " + encontrada.getStatus()
                );

            } else {
                System.out.println("\nBalança não encontrada!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar balança!");
            System.out.println(e.getMessage());
        }

        // =========================================================
        // FICHA TÉCNICA DA BALANÇA
        // =========================================================

        System.out.println("\n--- Ficha Técnica da Balança ---");

        System.out.println(
                "Número de série: " + balanca1.getNumeroSerie()
        );

        System.out.println(
                "OF: " + balanca1.getOrdemFabricacao()
        );

        System.out.println(
                "Modelo: " + balanca1.getModelo()
        );

        System.out.println(
                "Capacidade: " + balanca1.getCapacidade() + " Kg"
        );

        System.out.printf(
                "Dimensões: %.2f X %.2f m%n",
                balanca1.getDimensaoComprimento(),
                balanca1.getDimensaoLargura()
        );

        System.out.println(
                "Quantidade de células: "
                        + balanca1.getQuantidadeCelulas()
        );

        // =========================================================
        // CRIAÇÃO E INSTALAÇÃO DAS CÉLULAS NA BALANÇA EM MEMÓRIA
        // =========================================================

        CelulaCarga celula1 = new CelulaCarga("CL0001");
        CelulaCarga celula2 = new CelulaCarga("CL0002");
        CelulaCarga celula3 = new CelulaCarga("CL0003");

        balanca1.adicionarCelula(celula1);
        balanca1.adicionarCelula(celula2);


        // =========================================================
        // SUBSTITUIÇÃO DE CÉLULA NO BANCO DE DADOS
        // =========================================================
//
//        try {
//            repository.substituirCelulaNoBanco(
//                    "BAL-0010",
//                    "CL-TESTE-001",
//                    new CelulaCarga("CL-TESTE-003"),
//                    "Teste de substituição no sistema"
//            );
//
//            System.out.println(
//                    "Substituição registrada no banco com sucesso!"
//            );
//
//        } catch (SQLException e) {
//            System.out.println(
//                    "Erro ao substituir célula no banco!"
//            );
//            System.out.println(e.getMessage());
//        }


        try {
            System.out.println("\n--- Histórico de Substituições no Banco ---");

            var historico = repository.listarHistoricoSubstituicoes("BAL-0010");

            if (historico.isEmpty()) {
                System.out.println("Nenhuma substituição encontrada.");
            } else {
                for (SubstituicaoCelula substituicao : historico) {
                    System.out.println("Data: " + substituicao.getData());
                    System.out.println(
                            "Célula removida: "
                                    + substituicao.getCelulaRemovida().getNumeroSerie()
                    );
                    System.out.println(
                            "Célula instalada: "
                                    + substituicao.getCelulaInstalada().getNumeroSerie()
                    );
                    System.out.println("Motivo: " + substituicao.getMotivo());
                    System.out.println("-------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar histórico no banco!");
            System.out.println(e.getMessage());
        }


        // =========================================================
        // SUBSTITUIÇÃO DE UMA CÉLULA COM DEFEITO
        // =========================================================

        balanca1.substituirCelula(
                celula1.getNumeroSerie(),
                celula3,
                "Célula apresentou defeito"
        );

        // =========================================================
        // EXIBIÇÃO DAS CÉLULAS INSTALADAS
        // =========================================================

        System.out.println("\n--- Células após a substituição ---");

        for (CelulaCarga celula : balanca1.getCelulas()) {
            System.out.println(celula.getNumeroSerie());
        }

        System.out.println(
                "Quantidade: "
                        + balanca1.getQuantidadeCelulasInstaladas()
                        + "/"
                        + balanca1.getQuantidadeCelulas()
        );

        // =========================================================
        // HISTÓRICO DE SUBSTITUIÇÕES
        // =========================================================

        System.out.println("\n--- Histórico de Substituições ---");

        for (SubstituicaoCelula substituicao
                : balanca1.getHistoricoSubstituicoes()) {

            System.out.println(
                    "Data: " + substituicao.getData()
            );

            System.out.println(
                    "Célula removida: "
                            + substituicao.getCelulaRemovida()
                            .getNumeroSerie()
            );

            System.out.println(
                    "Célula instalada: "
                            + substituicao.getCelulaInstalada()
                            .getNumeroSerie()
            );

            System.out.println(
                    "Motivo: " + substituicao.getMotivo()
            );
        }

        // =========================================================
        // ALTERAÇÃO DE STATUS E TESTES DA BALANÇA
        // =========================================================

        System.out.println("\n--- Status da Balança ---");

        balanca1.iniciarTestes();

        System.out.println(
                "Status após iniciar testes: " + balanca1.getStatus()
        );

        TesteBalanca teste1 = new TesteBalanca(
                TipoTeste.CARGA,
                true,
                "Balança aprovada!"
        );

        balanca1.registrarTeste(teste1);
        if (!salvarTesteNoBanco(repository, "BAL-0010", teste1)) {
            System.out.println(
                    "Fluxo interrompido: não foi possível salvar todos os testes."
            );
            return;
        }

        teste1 = new TesteBalanca(
                TipoTeste.REPETIBILIDADE,
                true,
                "Balança aprovada!"
        );

        balanca1.registrarTeste(teste1);
        if (!salvarTesteNoBanco(repository, "BAL-0010", teste1)) {
            System.out.println(
                    "Fluxo interrompido: não foi possível salvar todos os testes."
            );
            return;
        }

        teste1 = new TesteBalanca(
                TipoTeste.VERIFICACAO_CELULAS,
                true,
                "Balança aprovada!"
        );

        balanca1.registrarTeste(teste1);
        if (!salvarTesteNoBanco(repository, "BAL-0010", teste1)) {
            System.out.println(
                    "Fluxo interrompido: não foi possível salvar todos os testes."
            );
            return;
        }

        teste1 = new TesteBalanca(
                TipoTeste.EXCENTRICIDADE,
                true,
                "Balança aprovada!"
        );

        balanca1.registrarTeste(teste1);
        if (!salvarTesteNoBanco(repository, "BAL-0010", teste1)) {
            System.out.println(
                    "Fluxo interrompido: não foi possível salvar todos os testes."
            );
            return;
        }


        try {
            boolean testesValidos =
                    repository.todosTestesValidos("BAL-0010");

            if (testesValidos) {
                balanca1.liberarBalanca();

                System.out.println(
                        "Status após liberação: " + balanca1.getStatus()
                );
            } else {
                System.out.println(
                        "Balança não liberada: existem testes ausentes, "
                                + "reprovados ou vencidos."
                );
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Não foi possível liberar a balança.");
            System.out.println(e.getMessage());
        }



        if (balanca1.getStatus() != br.com.scaletrack.model.StatusBalanca.LIBERADA) {
            System.out.println(
                    "Manutenção não iniciada: a balança não foi liberada."
            );
            return;
        }

        // =========================================================
        // MANUTENÇÃO
        // =========================================================

        System.out.println("\n--- Teste de Manutenção ---");

        Manutencao manutencao1 = new Manutencao(
                TipoManutencao.CORRETIVA,
                "Verificação e Aferição de peso"
        );

        try {
            repository.salvarManutencao("BAL-0010", manutencao1);
            System.out.println("Manutenção salva no BD");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar ao Banco de Dados!");
            System.out.println(e.getMessage());
        }

        balanca1.iniciarManutencao();

        System.out.println(
                "Status durante manutenção: " + balanca1.getStatus()
        );

//        Manutencao manutencao1 = new Manutencao(
//                TipoManutencao.CORRETIVA,
//                "Verificação e aferição de peso"
//        );

        balanca1.adicionarManutencao(manutencao1);

        balanca1.finalizarManutencao();

        System.out.println(
                "Status após a manutenção: " + balanca1.getStatus()
        );

        // =========================================================
        // HISTÓRICO DE MANUTENÇÕES
        // =========================================================

        System.out.println("\n--- Histórico de Manutenções ---");

        for (Manutencao manutencao
                : balanca1.getHistoricoManutencoes()) {

            System.out.println(
                    "Data: " + manutencao.getData()
            );

            System.out.println(
                    "Tipo: " + manutencao.getTipo()
            );

            System.out.println(
                    "Descrição: " + manutencao.getDescricao()
            );
        }

        // =========================================================
        // HISTÓRICO DE TESTES
        // =========================================================

        System.out.println("\n--- Histórico de Testes ---");

        for (TesteBalanca teste : balanca1.getHistoricoTestes()) {
            System.out.println("\nData: " + teste.getData());
            System.out.println("Tipo: " + teste.getTipo());
            System.out.println("Aprovado: " + teste.isAprovado());
            System.out.println("Observação: " + teste.getObservacao());
        }
    }


    private static boolean salvarTesteNoBanco(
            BalancaRepository repository,
            String numeroSerie,
            TesteBalanca teste
    ) {
        try {
            repository.salvarTesteBalanca(numeroSerie, teste);
            System.out.println("Teste " + teste.getTipo() + " salvo no banco!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar o teste " + teste.getTipo());
            System.out.println(e.getMessage());
            return false;
        }
    }

}
