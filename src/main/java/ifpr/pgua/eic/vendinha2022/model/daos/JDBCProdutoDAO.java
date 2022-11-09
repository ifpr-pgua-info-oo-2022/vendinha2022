package ifpr.pgua.eic.vendinha2022.model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import ifpr.pgua.eic.vendinha2022.model.FabricaConexao;
import ifpr.pgua.eic.vendinha2022.model.entities.Produto;
import ifpr.pgua.eic.vendinha2022.model.results.Result;

public class JDBCProdutoDAO implements ProdutoDAO{

    private FabricaConexao fabricaConexao;

    public JDBCProdutoDAO(FabricaConexao fabricaConexao){
        this.fabricaConexao = fabricaConexao;
    }


    @Override
    public Result criar(Produto produto) {
        //conectar no bd
        //criar o comando sql
        //executar o comando sql
        try{
            Connection con = fabricaConexao.getConnection();
            
            PreparedStatement pstm = con.prepareStatement("INSERT INTO produtos(nome,descricao,valor,quantidadeEstoque) VALUES (?,?,?,?)");

            pstm.setString(1, produto.getNome());
            pstm.setString(2, produto.getDescricao());
            pstm.setDouble(3, produto.getValor());
            pstm.setDouble(4, produto.getQuantidadeEstoque());

            pstm.executeUpdate();

            pstm.close();
            con.close();

            return Result.success("Produto inserido com sucesso!");

        }catch(SQLException e){
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Result atualizar(int id, Produto novoProduto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result remover(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Produto> listarTodos() {
        //conectar no banco
        //criar o comando sql
        //executar o comando sql
        //processar o resultado

        try{
            Connection con = fabricaConexao.getConnection();

            PreparedStatement pstm = con.prepareStatement("SELECT * FROM produtos");

            ResultSet resultSet = pstm.executeQuery();

            List<Produto> produtos = new ArrayList<>();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");
                double valor = resultSet.getDouble("valor");
                double quantidadeEstoque = resultSet.getDouble("quantidadeEstoque");

                Produto produto = new Produto(id,nome, descricao, valor, quantidadeEstoque);

                produtos.add(produto);
            }

            resultSet.close();
            pstm.close();
            con.close();
            return produtos;

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Produto buscarPorId(int id) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
