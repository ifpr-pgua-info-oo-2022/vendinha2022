package ifpr.pgua.eic.vendinha2022.model.repositories;

import java.util.List;

import ifpr.pgua.eic.vendinha2022.model.daos.ProdutoDAO;
import ifpr.pgua.eic.vendinha2022.model.entities.Produto;
import ifpr.pgua.eic.vendinha2022.model.results.Result;

public class ProdutoRepositorio {
    

    private ProdutoDAO dao;
    

    public ProdutoRepositorio(ProdutoDAO dao){
        this.dao = dao;
    }

    public Result cadastrar(String nome, String descricao, double valor, double quantidadeEstoque){

        if(valor < 0 ){
            return Result.fail("Valor inválido!");
        }

        if(quantidadeEstoque < 0){
            return Result.fail("Quantidade de estoque inválida!");
        }

        Produto produto = new Produto(nome, descricao, valor, quantidadeEstoque);

        return dao.criar(produto);

    }

    public List<Produto> mostrarTodos(){
        return dao.listarTodos();
    }
}
