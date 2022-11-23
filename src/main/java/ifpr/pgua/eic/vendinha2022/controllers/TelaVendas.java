package ifpr.pgua.eic.vendinha2022.controllers;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import ifpr.pgua.eic.vendinha2022.model.entities.ItemVenda;
import ifpr.pgua.eic.vendinha2022.model.entities.Venda;
import ifpr.pgua.eic.vendinha2022.model.repositories.VendaRepositorio;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class TelaVendas implements Initializable{
    
    @FXML
    private TableView<ItemVenda> tbItens;

    @FXML
    private TableView<Venda> tbVendas;

    @FXML
    private TableColumn<Venda, String> tbcCliente;

    @FXML
    private TableColumn<Venda, String> tbcDataHora;

    @FXML
    private TableColumn<Venda, String> tbcId;

    @FXML
    private TableColumn<ItemVenda, String> tbcItemProduto;

    @FXML
    private TableColumn<ItemVenda, String> tbcItemQuantidade;

    @FXML
    private TableColumn<ItemVenda, String> tbcItemValor;

    @FXML
    private TableColumn<Venda, String> tbcTotalVenda;
    
    private VendaRepositorio repositorio;

    public TelaVendas(VendaRepositorio repositorio){
        this.repositorio = repositorio;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        tbcCliente.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCliente().getNome()));
        tbcDataHora.setCellValueFactory(cell -> new SimpleStringProperty(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(cell.getValue().getDataHora())));
        tbcId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId()+""));
        tbcTotalVenda.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTotal()+""));
        
        tbcItemProduto.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduto().getNome()));
        tbcItemQuantidade.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getQuantidade()+""));
        tbcItemValor.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValorVenda()+""));
        
        atualizar();
    
    }

    private void atualizar(){
        tbVendas.getItems().clear();
        tbVendas.getItems().addAll(repositorio.listar());
    }

    @FXML
    public void mostrarItensVenda(MouseEvent evt){
        Venda selecionada = tbVendas.getSelectionModel().getSelectedItem();
    
        if(selecionada != null){
            tbItens.getItems().clear();
            tbItens.getItems().addAll(selecionada.getItens());
        }
    }



}
