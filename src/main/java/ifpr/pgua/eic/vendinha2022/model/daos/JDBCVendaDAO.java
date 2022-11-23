package ifpr.pgua.eic.vendinha2022.model.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ifpr.pgua.eic.vendinha2022.model.FabricaConexao;
import ifpr.pgua.eic.vendinha2022.model.entities.ItemVenda;
import ifpr.pgua.eic.vendinha2022.model.entities.Venda;
import ifpr.pgua.eic.vendinha2022.model.results.Result;

public class JDBCVendaDAO implements VendaDAO {

    private static final String INSERT = "INSERT INTO vendas_oo(dataHora,idCliente,total,desconto) VALUES (?,?,?,?)";
    private static final String INSERT_ITEM = "INSERT INTO itensvenda_oo(idVenda,idProduto,valor,quantidade) VALUES (?,?,?,?)";
    private static final String SELECT_ALL = "SELECT * FROM vendas_oo";

    private FabricaConexao fabricaConexoes;

    public JDBCVendaDAO(FabricaConexao fabricaConexoes) {
        this.fabricaConexoes = fabricaConexoes;
    }

    //Escreva aqui um comentário que explica o que o método faz


    @Override
    public Result criar(Venda venda) {
        try {
            Connection con = fabricaConexoes.getConnection();

            PreparedStatement pstm = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

            pstm.setTimestamp(1, Timestamp.valueOf(venda.getDataHora()));
            pstm.setInt(2, venda.getCliente().getId());
            pstm.setDouble(3, venda.getTotal());
            pstm.setDouble(4, venda.getDesconto());

            pstm.execute();

            // Pegar o id gerado por autoincremento
            ResultSet resultSet = pstm.getGeneratedKeys();
            resultSet.next();
            int idVenda = resultSet.getInt(1);

            PreparedStatement pstmItem = con.prepareStatement(INSERT_ITEM);

            for (ItemVenda item : venda.getItens()) {
                pstmItem.setInt(1, idVenda);
                pstmItem.setInt(2, item.getProduto().getId());
                pstmItem.setDouble(3, item.getValorVenda());
                pstmItem.setDouble(4, item.getQuantidade());

                pstmItem.execute();
            }

            pstmItem.close();
            pstm.close();

            con.close();

            return Result.success("Venda criada com sucesso!");

        } catch (SQLException e) {
            return Result.fail(e.getMessage());
        }
    }

    //Escreva aqui um comentário que explica o que o método faz

    private Venda montaVenda(ResultSet resultSet) throws SQLException {

        int id = resultSet.getInt("id");
        LocalDateTime dataHora = resultSet.getTimestamp("dataHora").toLocalDateTime();
        double total = resultSet.getDouble("total");
        double desconto = resultSet.getDouble("desconto");

        Venda venda = new Venda(id, dataHora, total, desconto);

        return venda;

    }

    //Escreva aqui um comentário que explica o que o método faz

    private List<ItemVenda> carregaItens(int idVenda) throws SQLException {
        List<ItemVenda> itens = new ArrayList<>();

        Connection con = fabricaConexoes.getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM itensvenda_oo WHERE idVenda=?");

        pstm.setInt(1, idVenda);

        ResultSet result = pstm.executeQuery();

        while (result.next()) {
            int idItem = result.getInt("id");
            double valor = result.getDouble("valor");
            double quantidade = result.getDouble("quantidade");

            ItemVenda item = new ItemVenda();
            item.setId(idItem);
            item.setValorVenda(valor);
            item.setQuantidade(quantidade);

            itens.add(item);
        }

        result.close();
        pstm.close();
        con.close();

        return itens;

    }

    //Escreva aqui um comentário que explica o que o método faz

    @Override
    public List<Venda> buscarTodas() {
        List<Venda> lista = new ArrayList<>();

        try {
            Connection con = fabricaConexoes.getConnection();

            PreparedStatement pstm = con.prepareStatement(SELECT_ALL);

            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                Venda venda = montaVenda(resultSet);
                venda.setItens(carregaItens(venda.getId()));
                lista.add(venda);
            }

            resultSet.close();
            pstm.close();
            con.close();

            return lista;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

}
