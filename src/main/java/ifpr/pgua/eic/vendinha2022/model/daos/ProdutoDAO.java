package ifpr.pgua.eic.vendinha2022.model.daos;

import java.util.List;

import ifpr.pgua.eic.vendinha2022.model.entities.Produto;
import ifpr.pgua.eic.vendinha2022.model.results.Result;

public interface ProdutoDAO {
    Result criar(Produto produto);
    Result atualizar(int id, Produto novoProduto);
    Result remover(int id);
    List<Produto> listarTodos();
    Produto buscarPorId(int id);
}
//CRUD
//Create -> Criar
//Retrieve -> Recuperar (listar, buscarPorId, buscarPorNome)
//Update -> atualizar
//Delete -> remover
