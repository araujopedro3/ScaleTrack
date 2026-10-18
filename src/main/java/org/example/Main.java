package org.example;

import br.com.scaletrack.model.Balanca;
import br.com.scaletrack.model.CelulaCarga;
import br.com.scaletrack.model.SubstituicaoCelula;
import br.com.scaletrack.model.Manutencao;
import br.com.scaletrack.model.TipoManutencao;
import br.com.scaletrack.model.TesteBalanca;
import br.com.scaletrack.model.TipoTeste;
import br.com.scaletrack.database.Conexao;
import java.sql.Connection;
import java.sql.SQLException;
import br.com.scaletrack.repository.BalancaRepository;
import org.w3c.dom.ls.LSOutput;

public class Main {

    public static void main(String[] args) {

        try {
            Connection conexao = Conexao.conectar();

            System.out.println("Conectado com sucesso!");
            conexao.close();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados!");
            System.out.println(e.getMessage());
        }

        // Criação da balança
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

        BalancaRepository repository = new BalancaRepository();

        try {
            System.out.println("\n--- Balanças Ativas ---");

            for (Balanca balanca : repository.listarTodas()) {
                System.out.println("Número de série: " + balanca.getNumeroSerie());
                System.out.println("Modelo: " + balanca.getModelo());
                System.out.println("Status: " + balanca.getStatus());
                System.out.println("-----------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar balanças!");
            System.out.println(e.getMessage());
        }

       // try {
           // repository.salvar(balanca2);
         //   System.out.println("Balança salva com sucesso!");

      //  } catch (SQLException e) {
        //    System.out.println("Erro ao salvar balança!");
        //    System.out.println(e.getMessage());
   //     }

        try{
            Balanca encontrada = repository.buscarPorNumeroSerie("BAL-0011");

            if(encontrada != null){
                System.out.println("Balança encontrada!");
                System.out.println("Número de série: " + encontrada.getNumeroSerie());
                System.out.println("Modelo: " + encontrada.getModelo());
                System.out.println("Status: " + encontrada.getStatus());

//                encontrada.iniciarTestes();
//                repository.atualizar(encontrada);
//                System.out.println("Novo status: " + encontrada.getStatus());

            } else {
                System.out.println("Balança não encontrada!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar balança: " + e.getMessage());
        }

//        try {
//            repository.reativar("BAL-0011");
//        } catch (SQLException e) {
//            System.out.println("Erro ao desativar a balança!");
//            System.out.println(e.getMessage());
//        }

        // Exibe os dados da balança
        System.out.println("\n--- Ficha Técnica da Balança ---");
        System.out.println("\nNúmero de série: " + balanca1.getNumeroSerie());
        System.out.println("OF: " + balanca1.getOrdemFabricacao());
        System.out.println("Modelo: " + balanca1.getModelo());
        System.out.println("Capacidade: " + balanca1.getCapacidade() + " Kg");

        System.out.printf(
                "Dimensões: %.2f X %.2f m%n",
                balanca1.getDimensaoComprimento(),
                balanca1.getDimensaoLargura()
        );

        System.out.println(
                "Quantidade de células: " + balanca1.getQuantidadeCelulas()
        );


        // Criação e instalação das células
        CelulaCarga celula1 = new CelulaCarga("CL0001");
        CelulaCarga celula2 = new CelulaCarga("CL0002");
        CelulaCarga celula3 = new CelulaCarga("CL0003");

        balanca1.adicionarCelula(celula1);
        balanca1.adicionarCelula(celula2);


        // Substituição de uma célula com defeito
        balanca1.substituirCelula(
                celula1.getNumeroSerie(),
                celula3,
                "Célula apresentou defeito"
        );


        // Exibe as células atualmente instaladas
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


        // Exibe o histórico de substituições
        System.out.println("\n--- Histórico de Substituições ---");

        for (SubstituicaoCelula substituicao :
                balanca1.getHistoricoSubstituicoes()) {

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
        }


        // =========================================================
        // ALTERAÇÃO DE STATUS DA BALANÇA
        // =========================================================

        System.out.println("\n--- Status da Balança ---");

        System.out.println("Status inicial: " + balanca1.getStatus());

        balanca1.iniciarTestes();
        System.out.println("Status após iniciar testes: " + balanca1.getStatus());

        TesteBalanca teste1 = new TesteBalanca(
                TipoTeste.CARGA, true, "Balança aprovada!"
        );
        balanca1.registrarTeste(teste1);

        teste1 = new TesteBalanca(
                TipoTeste.REPETIBILIDADE, true, "Balança aprovada!"
        );
        balanca1.registrarTeste(teste1);

        teste1 = new TesteBalanca(
                TipoTeste.VERIFICACAO_CELULAS, true, "Balança aprovada!"
        );
        balanca1.registrarTeste(teste1);

        teste1 = new TesteBalanca(
                TipoTeste.EXCENTRICIDADE, true, "Balança aprovada!"
        );
        balanca1.registrarTeste(teste1);

        balanca1.liberarBalanca();
        System.out.println("Status após liberação: " + balanca1.getStatus());


        // =========================================================
        // MANUTENÇÃO
        // =========================================================

        System.out.println("\n--- Teste de Manutenção ---");

        // Coloca a balança em manutenção
        balanca1.iniciarManutencao();

        System.out.println(
                "Status durante manutenção: " + balanca1.getStatus()
        );


        // Cria uma manutenção
        Manutencao manutencao1 = new Manutencao(
                TipoManutencao.CORRETIVA,
                "Verificação e aferição de peso"
        );


        // Registra a manutenção no histórico da balança
        balanca1.adicionarManutencao(manutencao1);


        // Finaliza a manutenção
        balanca1.finalizarManutencao();

        System.out.println(
                "Status após a manutenção: " + balanca1.getStatus()
        );


        // =========================================================
        // HISTÓRICO DE MANUTENÇÕES
        // =========================================================

        System.out.println("\n--- Histórico de Manutenções ---");

        for (Manutencao manutencao :
                balanca1.getHistoricoManutencoes()) {

            System.out.println("Data: " + manutencao.getData());
            System.out.println("Tipo: " + manutencao.getTipo());
            System.out.println("Descrição: " + manutencao.getDescricao());
        }

        System.out.println("\n--- Histórico de Testes ---");

        for (TesteBalanca teste : balanca1.getHistoricoTestes()) {

            System.out.println("\nData: " + teste.getData());
            System.out.println("Tipo: " + teste.getTipo());
            System.out.println("Aprovado: " + teste.isAprovado());
            System.out.println("Observação: " + teste.getObservacao());
        }
    }
}