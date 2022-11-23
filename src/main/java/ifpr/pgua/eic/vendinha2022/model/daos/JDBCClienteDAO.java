package ifpr.pgua.eic.vendinha2022.model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ifpr.pgua.eic.vendinha2022.model.FabricaConexao;
import ifpr.pgua.eic.vendinha2022.model.entities.Cliente;
import ifpr.pgua.eic.vendinha2022.model.results.Result;

public class JDBCClienteDAO implements ClienteDAO{

    private FabricaConexao fabricaConexao;

    public JDBCClienteDAO(FabricaConexao fabricaConexao){
        this.fabricaConexao = fabricaConexao;
    }


    @Override
    public Result criar(Cliente cliente) {
        try{
            
            Connection con = fabricaConexao.getConnection();

            PreparedStatement pstm = con.prepareStatement("INSERT INTO clientes_oo(nome,cpf,email,telefone) VALUES (?,?,?,?)");

            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getCpf());
            pstm.setString(3, cliente.getEmail());
            pstm.setString(4, cliente.getTelefone());

            pstm.executeUpdate();

            pstm.close();
            con.close();

            return Result.success("Cliente criado com sucesso!");


        }catch(SQLException nomeQueQuiser){
            System.out.println(nomeQueQuiser.getMessage());
            return Result.fail(nomeQueQuiser.getMessage());
        }
    }

    @Override
    public Result atualizar(int id, Cliente cliente) {
        try{
            Connection con = fabricaConexao.getConnection();

            PreparedStatement pstm = con.prepareStatement("UPDATE clientes_oo set email=?, telefone=? WHERE id=?");

            pstm.setString(1, cliente.getEmail());
            pstm.setString(2, cliente.getTelefone());
            pstm.setInt(3, id);
            
            pstm.executeUpdate();

            pstm.close();
            con.close();

            return Result.success("Cliente atualizado com sucesso!");

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return Result.fail(e.getMessage());
        }
    }




    @Override
    public Cliente buscarPorId(int id) {
        Cliente cliente = null;
        try{
            //criando uma conexão
            Connection con = fabricaConexao.getConnection(); 
            
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM clientes_oo WHERE id=?");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();
            
            while(rs.next()){
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String telefone = rs.getString("telefone");
                String cpf = rs.getString("cpf");

                cliente = new Cliente(id, nome, cpf, email, telefone);

            }


            rs.close();
            pstm.close();
            con.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }

        return cliente;
    }

    @Override
    public List<Cliente> buscarTodos() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try{

            Connection con = fabricaConexao.getConnection();

            PreparedStatement pstm = con.prepareStatement("SELECT * FROM clientes_oo");

            ResultSet rs = pstm.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String telefone = rs.getString("telefone");
                String cpf = rs.getString("cpf");

                Cliente cliente = new Cliente(id, nome, cpf, email, telefone);

                clientes.add(cliente);
            }
            rs.close();
            pstm.close();
            con.close();


        }catch(SQLException e){
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
        
        return Collections.unmodifiableList(clientes);
    }

    @Override
    public Result remover(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cliente buscarClienteDaVenda(int idVenda) {
        Cliente c = null;
        try{   

            Connection con = fabricaConexao.getConnection();

            PreparedStatement pstm = con.prepareStatement("SELECT idCliente FROM vendas_oo WHERE id=?");

            pstm.setInt(1, idVenda);

            ResultSet resultSetIdCliente = pstm.executeQuery();
            resultSetIdCliente.next();

            int idCliente = resultSetIdCliente.getInt("idCliente");

            c = buscarPorId(idCliente);

            resultSetIdCliente.close();
            pstm.close();
            con.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return c;
    }
    
}
