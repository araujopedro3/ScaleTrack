
// packages
package br.com.scaletrack.repository;

// imports dentro do package pai
import br.com.scaletrack.database.Conexao;
import br.com.scaletrack.model.Balanca;
import br.com.scaletrack.model.CelulaCarga;
import br.com.scaletrack.model.Manutencao;
import br.com.scaletrack.model.StatusBalanca;
import br.com.scaletrack.model.SubstituicaoCelula;
import br.com.scaletrack.model.TesteBalanca;
import br.com.scaletrack.model.TipoTeste;

// imports do java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class BalancaRepository {

    // =========================================================
    // SALVAR BALANÇA
    // =========================================================

    public void salvar(Balanca balanca) throws SQLException {

        String sql = """
                INSERT INTO balancas (
                    numero_serie,
                    ordem_fabricacao,
                    modelo,
                    capacidade,
                    dimensao_comprimento,
                    dimensao_largura,
                    quantidade_celulas,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, balanca.getNumeroSerie());
            statement.setString(2, balanca.getOrdemFabricacao());
            statement.setString(3, balanca.getModelo());
            statement.setInt(4, balanca.getCapacidade());
            statement.setDouble(5, balanca.getDimensaoComprimento());
            statement.setDouble(6, balanca.getDimensaoLargura());
            statement.setInt(7, balanca.getQuantidadeCelulas());
            statement.setString(8, balanca.getStatus().toString());

            statement.executeUpdate();
        }
    }

    // =========================================================
    // BUSCAR BALANÇA POR NÚMERO DE SÉRIE
    // =========================================================

    public Balanca buscarPorNumeroSerie(String numeroSerie)
            throws SQLException {

        String sql = """
                SELECT *
                FROM balancas
                WHERE numero_serie = ?
                  AND ativo = TRUE
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerie);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {

                    String serie =
                            resultado.getString("numero_serie");

                    String modelo =
                            resultado.getString("modelo");

                    String ordemFabricacao =
                            resultado.getString("ordem_fabricacao");

                    int capacidade =
                            resultado.getInt("capacidade");

                    double comprimento =
                            resultado.getDouble("dimensao_comprimento");

                    double largura =
                            resultado.getDouble("dimensao_largura");

                    int quantidadeCelulas =
                            resultado.getInt("quantidade_celulas");

                    StatusBalanca status = StatusBalanca.valueOf(
                            resultado.getString("status")
                    );

                    return new Balanca(
                            serie,
                            ordemFabricacao,
                            modelo,
                            capacidade,
                            comprimento,
                            largura,
                            quantidadeCelulas,
                            status
                    );
                }

                return null;
            }
        }
    }

    // =========================================================
    // ATUALIZAR BALANÇA
    // =========================================================

    public void atualizar(Balanca balanca) throws SQLException {

        String sql = """
                UPDATE balancas
                SET status = ?
                WHERE numero_serie = ?
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, balanca.getStatus().toString());
            statement.setString(2, balanca.getNumeroSerie());

            statement.executeUpdate();
        }
    }

    // =========================================================
    // DESATIVAR BALANÇA
    // =========================================================

    public void desativar(String numeroSerie) throws SQLException {

        String sql = """
                UPDATE balancas
                SET ativo = FALSE
                WHERE numero_serie = ?
                  AND ativo = TRUE
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerie);

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Balança desativada com sucesso!");
            } else {
                System.out.println(
                        "Balança não encontrada ou já está desativada."
                );
            }
        }
    }

    // =========================================================
    // REATIVAR BALANÇA
    // =========================================================

    public void reativar(String numeroSerie) throws SQLException {

        String sql = """
                UPDATE balancas
                SET ativo = TRUE
                WHERE numero_serie = ?
                  AND ativo = FALSE
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerie);

            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Balança reativada com sucesso!");
            } else {
                System.out.println(
                        "Balança não encontrada ou já está ativa."
                );
            }
        }
    }

    // =========================================================
    // LISTAR BALANÇAS ATIVAS
    // =========================================================

    public List<Balanca> listarTodas() throws SQLException {

        String sql = """
                SELECT *
                FROM balancas
                WHERE ativo = TRUE
                """;

        List<Balanca> balancas = new ArrayList<>();

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()
        ) {
            while (resultado.next()) {

                String numeroSerie =
                        resultado.getString("numero_serie");

                String ordemFabricacao =
                        resultado.getString("ordem_fabricacao");

                String modelo =
                        resultado.getString("modelo");

                int capacidade =
                        resultado.getInt("capacidade");

                double comprimento =
                        resultado.getDouble("dimensao_comprimento");

                double largura =
                        resultado.getDouble("dimensao_largura");

                int quantidadeCelulas =
                        resultado.getInt("quantidade_celulas");

                StatusBalanca status = StatusBalanca.valueOf(
                        resultado.getString("status")
                );

                Balanca balanca = new Balanca(
                        numeroSerie,
                        ordemFabricacao,
                        modelo,
                        capacidade,
                        comprimento,
                        largura,
                        quantidadeCelulas,
                        status
                );

                balancas.add(balanca);
            }
        }

        return balancas;
    }

    // =========================================================
    // SALVAR CÉLULA DE CARGA
    // =========================================================

    public void salvarCelula(
            CelulaCarga celula,
            String numeroSerieBalanca
    ) throws SQLException {

        if (celula == null) {
            throw new IllegalArgumentException(
                    "A célula não pode ser nula."
            );
        }

        if (numeroSerieBalanca == null
                || numeroSerieBalanca.isBlank()) {
            throw new IllegalArgumentException(
                    "O número de série da balança é obrigatório."
            );
        }

        String sqlVerificarBalanca = """
                SELECT numero_serie
                FROM balancas
                WHERE numero_serie = ?
                  AND ativo = TRUE
                """;

        String sqlInserirCelula = """
                INSERT INTO celulas_carga (
                    numero_serie,
                    balanca_numero_serie
                )
                VALUES (?, ?)
                """;

        String sqlVincularCelula = """
                INSERT INTO balanca_celulas (
                    balanca_numero_serie,
                    celula_numero_serie
                )
                VALUES (?, ?)
                """;

        try (Connection conexao = Conexao.conectar()) {

            conexao.setAutoCommit(false);

            try {
                // Verifica se a balança existe e está ativa.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(sqlVerificarBalanca)
                ) {
                    stmt.setString(1, numeroSerieBalanca);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Balança não encontrada ou inativa."
                            );
                        }
                    }
                }

                // Cadastra a célula no registro permanente.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(sqlInserirCelula)
                ) {
                    stmt.setString(1, celula.getNumeroSerie());
                    stmt.setString(2, numeroSerieBalanca);
                    stmt.executeUpdate();
                }

                // Registra a instalação da célula na balança.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(sqlVincularCelula)
                ) {
                    stmt.setString(1, numeroSerieBalanca);
                    stmt.setString(2, celula.getNumeroSerie());
                    stmt.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException | RuntimeException e) {

                try {
                    conexao.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }

                throw e;

            } finally {
                conexao.setAutoCommit(true);
            }
        }
    }

    // =========================================================
    // BUSCAR CÉLULA POR NÚMERO DE SÉRIE
    // =========================================================

    public CelulaCarga buscarCelulaPorNumeroSerie(String numeroSerie)
            throws SQLException {

        String sql = """
                SELECT numero_serie
                FROM celulas_carga
                WHERE numero_serie = ?
                """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerie);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return new CelulaCarga(
                            resultado.getString("numero_serie")
                    );
                }

                return null;
            }
        }
    }

    // =========================================================
    // LISTAR CÉLULAS POR BALANÇA
    // =========================================================

    public List<CelulaCarga> listarCelulasPorBalanca(
            String numeroSerieBalanca
    ) throws SQLException {

        String sql = """
                SELECT c.numero_serie
                FROM celulas_carga c
                INNER JOIN balanca_celulas bc
                    ON bc.celula_numero_serie = c.numero_serie
                WHERE bc.balanca_numero_serie = ?
                ORDER BY c.numero_serie
                """;

        List<CelulaCarga> celulas = new ArrayList<>();

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerieBalanca);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {

                    CelulaCarga celula = new CelulaCarga(
                            resultado.getString("numero_serie")
                    );

                    celulas.add(celula);
                }
            }
        }

        return celulas;
    }

    // =========================================================
    // BUSCAR BALANÇA COM SUAS CÉLULAS
    // =========================================================

    public Balanca buscarComCelulas(String numeroSerie)
            throws SQLException {

        Balanca balanca = buscarPorNumeroSerie(numeroSerie);

        if (balanca == null) {
            return null;
        }

        List<CelulaCarga> celulas =
                listarCelulasPorBalanca(numeroSerie);

        for (CelulaCarga celula : celulas) {
            balanca.adicionarCelula(celula);
        }

        return balanca;
    }

    // =========================================================
    // SUBSTITUIR CÉLULA NO BANCO
    // =========================================================

    public void substituirCelulaNoBanco(
            String numeroSerieBalanca,
            String numeroSerieAntiga,
            CelulaCarga novaCelula,
            String motivo
    ) throws SQLException {

        if (numeroSerieBalanca == null
                || numeroSerieBalanca.isBlank()
                || numeroSerieAntiga == null
                || numeroSerieAntiga.isBlank()
                || novaCelula == null
                || novaCelula.getNumeroSerie() == null
                || novaCelula.getNumeroSerie().isBlank()
                || motivo == null
                || motivo.isBlank()) {

            throw new IllegalArgumentException(
                    "Os dados da substituição são obrigatórios."
            );
        }

        if (numeroSerieAntiga.equals(novaCelula.getNumeroSerie())) {
            throw new IllegalArgumentException(
                    "A nova célula deve ser diferente da célula antiga."
            );
        }

        String sqlVerificarBalanca = """
                SELECT numero_serie
                FROM balancas
                WHERE numero_serie = ?
                  AND ativo = TRUE
                FOR UPDATE
                """;

        String sqlVerificarCelulaAntiga = """
                SELECT bc.celula_numero_serie
                FROM balanca_celulas bc
                INNER JOIN celulas_carga c
                    ON c.numero_serie = bc.celula_numero_serie
                WHERE bc.balanca_numero_serie = ?
                  AND bc.celula_numero_serie = ?
                FOR UPDATE
                """;

        String sqlVerificarNovaCelula = """
                SELECT numero_serie
                FROM celulas_carga
                WHERE numero_serie = ?
                """;

        String sqlInserirCelula = """
                INSERT INTO celulas_carga (
                    numero_serie,
                    balanca_numero_serie
                )
                VALUES (?, ?)
                """;

        String sqlRemoverVinculoAntigo = """
                DELETE FROM balanca_celulas
                WHERE balanca_numero_serie = ?
                  AND celula_numero_serie = ?
                """;

        String sqlInserirVinculoNovo = """
                INSERT INTO balanca_celulas (
                    balanca_numero_serie,
                    celula_numero_serie
                )
                VALUES (?, ?)
                """;

        /// ESTUDAR DAQUI PARA BAIXO!!!!!!! ------------

        String sqlInserirHistorico = """
                INSERT INTO substituicoes_celulas (
                    balanca_numero_serie,
                    celula_removida_numero_serie,
                    celula_instalada_numero_serie,
                    motivo
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar()) {

            conexao.setAutoCommit(false);

            try {
                // 1. Confirma que a balança existe e está ativa.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(sqlVerificarBalanca)
                ) {
                    stmt.setString(1, numeroSerieBalanca);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Balança não encontrada ou inativa."
                            );
                        }
                    }
                }

                // 2. Confirma que a célula antiga está instalada na balança.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(
                                        sqlVerificarCelulaAntiga
                                )
                ) {
                    stmt.setString(1, numeroSerieBalanca);
                    stmt.setString(2, numeroSerieAntiga);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "A célula antiga não está instalada "
                                            + "nessa balança."
                            );
                        }
                    }
                }

                // 3. Confirma que a nova célula ainda não existe.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(
                                        sqlVerificarNovaCelula
                                )
                ) {
                    stmt.setString(1, novaCelula.getNumeroSerie());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException(
                                    "A nova célula já está cadastrada. "
                                            + "Escolha outro número de série."
                            );
                        }
                    }
                }

                // 4. Cadastra a nova célula.
                // A coluna antiga continua obrigatória no esquema atual.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(sqlInserirCelula)
                ) {
                    stmt.setString(1, novaCelula.getNumeroSerie());
                    stmt.setString(2, numeroSerieBalanca);
                    stmt.executeUpdate();
                }

                // 5. Remove somente o vínculo antigo.
                // A célula antiga continua cadastrada para o histórico.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(
                                        sqlRemoverVinculoAntigo
                                )
                ) {
                    stmt.setString(1, numeroSerieBalanca);
                    stmt.setString(2, numeroSerieAntiga);

                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException(
                                "Não foi possível remover o vínculo antigo."
                        );
                    }
                }

                // 6. Vincula a nova célula à balança.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(
                                        sqlInserirVinculoNovo
                                )
                ) {
                    stmt.setString(1, numeroSerieBalanca);
                    stmt.setString(2, novaCelula.getNumeroSerie());
                    stmt.executeUpdate();
                }

                // 7. Registra o histórico.
                // O MySQL preenche data_substituicao automaticamente.
                try (
                        PreparedStatement stmt =
                                conexao.prepareStatement(sqlInserirHistorico)
                ) {
                    stmt.setString(1, numeroSerieBalanca);
                    stmt.setString(2, numeroSerieAntiga);
                    stmt.setString(3, novaCelula.getNumeroSerie());
                    stmt.setString(4, motivo);
                    stmt.executeUpdate();
                }

                conexao.commit();

            } catch (SQLException | RuntimeException e) {

                try {
                    conexao.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }

                throw e;

            } finally {
                conexao.setAutoCommit(true);
            }
        }
    }


    public List<SubstituicaoCelula> listarHistoricoSubstituicoes(
            String numeroSerieBalanca
    ) throws SQLException {

        String sql = """
            SELECT
                celula_removida_numero_serie,
                celula_instalada_numero_serie,
                motivo,
                data_substituicao
            FROM substituicoes_celulas
            WHERE balanca_numero_serie = ?
            ORDER BY data_substituicao DESC
            """;

        List<SubstituicaoCelula> historico = new ArrayList<>();

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerieBalanca);

            try (ResultSet resultado = statement.executeQuery()) {

                while (resultado.next()) {

                    String serieAntiga = resultado.getString(
                            "celula_removida_numero_serie"
                    );

                    String serieNova = resultado.getString(
                            "celula_instalada_numero_serie"
                    );

                    String motivo = resultado.getString("motivo");

                    LocalDate data = resultado.getTimestamp(
                            "data_substituicao"
                    ).toLocalDateTime().toLocalDate();

                    SubstituicaoCelula substituicao =
                            new SubstituicaoCelula(
                                    new CelulaCarga(serieAntiga),
                                    new CelulaCarga(serieNova),
                                    motivo
                            );

                    historico.add(substituicao);
                }
            }
        }

        return historico;
    }


    public void salvarManutencao(
            String numeroSerieBalanca,
            Manutencao manutencao
    ) throws SQLException {

        if (numeroSerieBalanca == null || numeroSerieBalanca.isBlank()) {
            throw new IllegalArgumentException(
                    "O número de série da balança é obrigatório."
            );
        }

        if (manutencao == null) {
            throw new IllegalArgumentException(
                    "A manutenção não pode ser nula."
            );
        }

        String sql = """
            INSERT INTO manutencoes (
                balanca_numero_serie,
                tipo,
                descricao,
                data_manutencao
            )
            VALUES (?, ?, ?, ?)
            """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerieBalanca);
            statement.setString(2, manutencao.getTipo().name());
            statement.setString(3, manutencao.getDescricao());
            statement.setDate(
                    4,
                    java.sql.Date.valueOf(manutencao.getData())
            );

            statement.executeUpdate();
        }
    }


    public void salvarTesteBalanca(
            String numeroSerieBalanca,
            TesteBalanca teste
    ) throws SQLException {

        if (numeroSerieBalanca == null || numeroSerieBalanca.isBlank()) {
            throw new IllegalArgumentException(
                    "O número de série da balança é obrigatório."
            );
        }

        if (teste == null) {
            throw new IllegalArgumentException(
                    "O teste não pode ser nulo."
            );
        }

        String sql = """
            INSERT INTO testes_balanca (
                balanca_numero_serie,
                tipo,
                aprovado,
                data_teste,
                observacao
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerieBalanca);
            statement.setString(2, teste.getTipo().name());
            statement.setBoolean(3, teste.isAprovado());
            statement.setDate(
                    4,
                    java.sql.Date.valueOf(teste.getData())
            );
            statement.setString(5, teste.getObservacao());

            statement.executeUpdate();
        }
    }


    // =========================================================
    // LISTAR TESTES POR BALANÇA
    // =========================================================

    public List<TesteBalanca> listarTestesPorBalanca(
            String numeroSerieBalanca
    ) throws SQLException {

        String sql = """
                SELECT tipo, aprovado, data_teste, observacao
                FROM testes_balanca
                WHERE balanca_numero_serie = ?
                ORDER BY data_teste DESC, id DESC
                """;

        List<TesteBalanca> testes = new ArrayList<>();

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, numeroSerieBalanca);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {

                    TipoTeste tipo = TipoTeste.valueOf(
                            resultado.getString("tipo")
                    );

                    boolean aprovado = resultado.getBoolean("aprovado");

                    LocalDate data = resultado.getDate("data_teste")
                            .toLocalDate();

                    String observacao = resultado.getString("observacao");

                    TesteBalanca teste = new TesteBalanca(
                            tipo,
                            aprovado,
                            data,
                            observacao
                    );

                    testes.add(teste);
                }
            }
        }

        return testes;
    }


    public boolean todosTestesValidos(String numeroSerieBalanca)
            throws SQLException {

        List<TesteBalanca> testes =
                listarTestesPorBalanca(numeroSerieBalanca);

        TipoTeste[] tiposObrigatorios = {
                TipoTeste.CARGA,
                TipoTeste.REPETIBILIDADE,
                TipoTeste.EXCENTRICIDADE,
                TipoTeste.VERIFICACAO_CELULAS
        };

        LocalDate hoje = LocalDate.now();

        for (TipoTeste tipo : tiposObrigatorios) {
            TesteBalanca ultimoTeste = null;

            for (TesteBalanca teste : testes) {
                if (teste.getTipo() == tipo) {
                    ultimoTeste = teste;
                    break;
                }
            }

            if (ultimoTeste == null
                    || !ultimoTeste.isAprovado()
                    || ultimoTeste.getData().plusMonths(3).isBefore(hoje)) {
                return false;
            }
        }

        return true;
    }


}
