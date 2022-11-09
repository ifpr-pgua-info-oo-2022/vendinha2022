package ifpr.pgua.eic.vendinha2022;

import java.util.List;

import ifpr.pgua.eic.vendinha2022.model.FabricaConexao;
import ifpr.pgua.eic.vendinha2022.model.daos.JDBCProdutoDAO;
import ifpr.pgua.eic.vendinha2022.model.daos.ProdutoDAO;
import ifpr.pgua.eic.vendinha2022.model.entities.Produto;
import ifpr.pgua.eic.vendinha2022.model.results.Result;

public class Teste {
    

    public static void main(String[] args) {
        
        ProdutoDAO dao = new JDBCProdutoDAO(FabricaConexao.getInstance());

        List<Produto> produtos = dao.listarTodos();

        for(Produto p:produtos){
            System.out.println(p.getId()+" "+p.getNome());
        }


    }
}
