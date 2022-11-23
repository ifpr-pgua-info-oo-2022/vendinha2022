package ifpr.pgua.eic.vendinha2022.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ifpr.pgua.eic.vendinha2022.model.entities.Cliente;
import ifpr.pgua.eic.vendinha2022.model.entities.ItemVenda;
import ifpr.pgua.eic.vendinha2022.model.entities.Produto;
import ifpr.pgua.eic.vendinha2022.model.repositories.ClienteRepositorio;
import ifpr.pgua.eic.vendinha2022.model.repositories.ProdutoRepositorio;
import ifpr.pgua.eic.vendinha2022.model.repositories.VendaRepositorio;
import ifpr.pgua.eic.vendinha2022.model.results.Result;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TelaNovaVenda extends BaseController implements Initializable {

    @FXML
    private Button btAdicionar;

    @FXML
    private Button btFinalizar;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private ComboBox<Produto> cbProduto;

    @FXML
    private Label lbTotal;

    @FXML
    private TableView<ItemVenda> tbItens;

    @FXML
    private TableColumn<ItemVenda,String> tbcProduto;

    @FXML
    private TableColumn<ItemVenda,String> tbcQuantidade;

    @FXML
    private TableColumn<ItemVenda,String> tbcValorUnitario;

    @FXML
    private TableColumn<ItemVenda,String> tbcValorItem;


    @FXML
    private TextField tfData;

    @FXML
    private TextField tfQuantidade;

    private VendaRepositorio repositorioVenda;
    private ClienteRepositorio repositorioCliente;
    private ProdutoRepositorio repositorioProduto;


    private ArrayList<ItemVenda> itens;


    public TelaNovaVenda(VendaRepositorio repositorioVenda, ClienteRepositorio repositorioCliente, ProdutoRepositorio repositorioProduto ){
        this.repositorioVenda = repositorioVenda;
        this.repositorioProduto = repositorioProduto;
        this.repositorioCliente = repositorioCliente;
        this.itens = new ArrayList<>();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.cbCliente.getItems().addAll(repositorioCliente.listar());
        this.cbProduto.getItems().addAll(repositorioProduto.getProdutos());

        tbcProduto.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getProduto().getNome()));
        tbcQuantidade.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getQuantidade()+""));
        tbcValorUnitario.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getProduto().getValor()+""));
        tbcValorItem.setCellValueFactory(celula -> new SimpleStringProperty(celula.getValue().getProduto().getValor()*celula.getValue().getQuantidade()+""));


        tfData.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm").format(LocalDateTime.now()));
    }

    private void atualizar(){
        tbItens.getItems().clear();
        tbItens.getItems().addAll(itens);

        double total=0.0;
        for(ItemVenda item:itens){
            total += item.getQuantidade()*item.getValorVenda();
        }

        lbTotal.setText("R$ "+total);
    }

    private void limparEntradaItem(){
        cbProduto.getSelectionModel().clearSelection();
        tfQuantidade.clear();
    }



    @FXML
    private void adicionarItem(){

        Produto produto = cbProduto.getSelectionModel().getSelectedItem();
        String sQuantidade = tfQuantidade.getText();

        double quantidade = 0.0;

        try{
            quantidade = Double.valueOf(sQuantidade);
        }catch(NumberFormatException e){
            showMessage(Result.fail("Quantidade inválida!"));
        }

        if(quantidade <= 0){
            showMessage(Result.fail("Quantidade inválida!"));
        }

        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setValorVenda(produto.getValor());

        itens.add(item);

        atualizar();
        limparEntradaItem();

    }

    @FXML
    private void finalizar(){

        Cliente cliente = cbCliente.getSelectionModel().getSelectedItem();
        if(cliente != null){
            Result resultado = repositorioVenda.cadastrar(LocalDateTime.now(), cliente, itens);
            showMessage(resultado);
        }
        
    }

}

