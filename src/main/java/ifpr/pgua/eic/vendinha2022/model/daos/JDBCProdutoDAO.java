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
            
            PreparedStatement pstm = con.prepareStatement("INSERT INTO produtos_oo(nome,descricao,valor,quantidadeEstoque) VALUES (?,?,?,?)");

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
        try{
            //criando uma conexão
            Connection con = fabricaConexao.getConnection(); 
            
            //preparando o comando sql
            PreparedStatement pstm = con.prepareStatement("UPDATE produtos_oo set nome=?, descricao=?, valor=?, quantidadeEstoque=? WHERE id=?");
            
            //ajustando os parâmetros do comando
            pstm.setString(1, novoProduto.getNome());
            pstm.setString(2, novoProduto.getDescricao());
            pstm.setDouble(3, novoProduto.getValor());
            pstm.setDouble(4, novoProduto.getQuantidadeEstoque());
            pstm.setInt(5, id);

            pstm.execute();

            pstm.close();
            con.close();

            return Result.success("Produto atualizado com sucesso!");

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return Result.fail(e.getMessage());
        }
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

            PreparedStatement pstm = con.prepareStatement("SELECT * FROM produtos_oo");

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
        try{
            //criando uma conexão
            Connection con = fabricaConexao.getConnection(); 
            
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM produtos_oo WHERE id=?");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();
            Produto produto = null; 

            while(rs.next()){
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double valor = rs.getDouble("valor");
                double quantidadeEstoque = rs.getDouble("quantidadeEstoque");

                produto = new Produto(id,nome, descricao, valor, quantidadeEstoque);
 
            }

            rs.close();
            pstm.close();
            con.close();

            return produto;

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Produto buscarProdutoItem(int itemId) {
        Produto produto = null;

        try{    
            Connection con = fabricaConexao.getConnection();

            PreparedStatement pstm = con.prepareStatement("SELECT idProduto FROM itensvenda_oo WHERE id=?");

            pstm.setInt(1, itemId);

            ResultSet resultSetidProduto = pstm.executeQuery();
            resultSetidProduto.next();

            int idProduto = resultSetidProduto.getInt("idProduto");

            resultSetidProduto.close();
            pstm.close();
            con.close();

            produto = buscarPorId(idProduto);

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return produto;
    }
    
}