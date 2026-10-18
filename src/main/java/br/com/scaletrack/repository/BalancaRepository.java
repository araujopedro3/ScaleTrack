    package br.com.scaletrack.repository;

    import br.com.scaletrack.model.Balanca;
    import br.com.scaletrack.database.Conexao;

    import java.sql.*;
    import java.util.List;

    import br.com.scaletrack.model.StatusBalanca;

    import java.util.ArrayList;

    public class BalancaRepository {

        public void salvar(Balanca balanca)
            throws SQLException {
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
                VALUES(?, ?, ?, ?, ?, ?, ?, ?)
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

        public Balanca buscarPorNumeroSerie(String numeroSerie)
            throws SQLException {
            String sql = "SELECT * FROM balancas WHERE numero_serie = ? AND ativo = TRUE";

            try (
                    Connection conexao = Conexao.conectar();
                    PreparedStatement statement = conexao.prepareStatement(sql)
                    ) {
                statement.setString(1, numeroSerie);

                ResultSet resultado = statement.executeQuery();
                if(resultado.next()) {
                    String serie = resultado.getString("numero_serie");
                    String modelo =  resultado.getString("modelo");

                    String ordemFabricacao =  resultado.getString("ordem_fabricacao");
                    int capacidade =  resultado.getInt("capacidade");
                    double comprimento =  resultado.getDouble("dimensao_comprimento");
                    double largura =  resultado.getDouble("dimensao_largura");
                    int quantidadeCelulas = resultado.getInt("quantidade_celulas");

                    StatusBalanca status = StatusBalanca.valueOf(resultado.getString("status"));

                    Balanca balanca = new Balanca(
                            serie,
                            ordemFabricacao,
                            modelo,
                            capacidade,
                            comprimento,
                            largura,
                            quantidadeCelulas,
                            status
                    );

                    return balanca;
                }
                return null;
            }
        }

        public void atualizar(Balanca balanca)
            throws SQLException {
            String sql = """
                        
                    UPDATE balancas
                        SET STATUS = ?
                        WHERE numero_serie = ?
                        """;

            try (
                Connection conexao = Conexao.
                    conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)
            ) {
                statement.setString(1, balanca.getStatus().toString());
                statement.setString(2, balanca.getNumeroSerie());

                statement.
        executeUpdate();

            }
                }

            public void desativar(String numeroSerie)
                throws SQLException {
                    String sql = """
                            UPDATE balancas
                            SET ativo = false
                            WHERE numero_serie = ? AND ativo = TRUE
                            """;

                    try (
                            Connection conexao = Conexao.conectar();
                            PreparedStatement statement = conexao.prepareStatement(sql)
                    ) {
                        statement.setString(1, numeroSerie);
                        int linhasAfetadas = statement.executeUpdate();

                        if (linhasAfetadas > 0) {
                            System.out.println("Balanca desativada com sucesso!");
                        } else {
                            System.out.println("Balança não encontrada ou já está desativada");
                        }
                    }
        }

            public void reativar(String numeroSerie)
                throws SQLException {
            String sql = """
                        UPDATE balancas
                        SET ativo = true
                        WHERE numero_serie = ? AND ativo = FALSE
            """;

                    try (
                            Connection conexao = Conexao.conectar();
                            PreparedStatement statement = conexao.prepareStatement(sql)
                            )
                    {
                        statement.setString(1, numeroSerie);
                        int linhasAfetadas = statement.executeUpdate();

                        if (linhasAfetadas > 0) {
                            System.out.println("Balanca reativada com sucesso!");
                        } else {
                            System.out.println("Balança não encontrada ou já está ativa!");
                        }
                    }
            }

            public List<Balanca> listarTodas()
                throws SQLException {

                    String sql = """
                            SELECT * FROM balancas
                            WHERE ativo = TRUE
                    """;

                    List<Balanca> balancas = new ArrayList<>();

                    try (
                            Connection conexao = Conexao.conectar();
                            PreparedStatement statement = conexao.prepareStatement(sql)
                            ) {

                        ResultSet resultado = statement.executeQuery();

                        while (resultado.next()) {

                            String numeroSerie = resultado.getString("numero_serie");
                            String ordemFabricacao = resultado.getString("ordem_fabricacao");
                            String modelo = resultado.getString("modelo");
                            int capacidade = resultado.getInt("capacidade");
                            double comprimento = resultado.getDouble("dimensao_comprimento");
                            double largura = resultado.getDouble("dimensao_largura");
                            int quantidadeCelulas = resultado.getInt("quantidade_celulas");

                            StatusBalanca status = StatusBalanca.valueOf
                                    (resultado.getString("status"));

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

                        return balancas;
                    }
            }
    }
