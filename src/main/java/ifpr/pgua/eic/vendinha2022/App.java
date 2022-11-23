package ifpr.pgua.eic.vendinha2022;

import ifpr.pgua.eic.vendinha2022.controllers.TelaClientes;
import ifpr.pgua.eic.vendinha2022.controllers.TelaNovaVenda;
import ifpr.pgua.eic.vendinha2022.controllers.TelaPrincipal;
import ifpr.pgua.eic.vendinha2022.controllers.TelaProdutos;
import ifpr.pgua.eic.vendinha2022.controllers.TelaVendas;
import ifpr.pgua.eic.vendinha2022.model.FabricaConexao;
import ifpr.pgua.eic.vendinha2022.model.daos.ClienteDAO;
import ifpr.pgua.eic.vendinha2022.model.daos.JDBCClienteDAO;
import ifpr.pgua.eic.vendinha2022.model.daos.JDBCProdutoDAO;
import ifpr.pgua.eic.vendinha2022.model.daos.JDBCVendaDAO;
import ifpr.pgua.eic.vendinha2022.model.daos.ProdutoDAO;
import ifpr.pgua.eic.vendinha2022.model.daos.VendaDAO;
import ifpr.pgua.eic.vendinha2022.model.repositories.ClienteRepositorio;
import ifpr.pgua.eic.vendinha2022.model.repositories.ProdutoRepositorio;
import ifpr.pgua.eic.vendinha2022.model.repositories.VendaRepositorio;
import ifpr.pgua.eic.vendinha2022.utils.BaseAppNavigator;
import ifpr.pgua.eic.vendinha2022.utils.ScreenRegistryFXML;


/**
 * JavaFX App
 */
public class App extends BaseAppNavigator {

    private FabricaConexao fabricaConexao = FabricaConexao.getInstance();

    private ClienteDAO clienteDao;
    private ClienteRepositorio clienteRepositorio;

    private ProdutoDAO produtoDao;
    private ProdutoRepositorio produtoRepositorio;

    private VendaDAO vendaDao;
    private VendaRepositorio vendaRepositorio;


    @Override
    public void init() throws Exception {
        // TODO Auto-generated method stub
        super.init();

        clienteDao = new JDBCClienteDAO(fabricaConexao);
        clienteRepositorio = new ClienteRepositorio(clienteDao);


        produtoDao = new JDBCProdutoDAO(fabricaConexao);
        produtoRepositorio = new ProdutoRepositorio(produtoDao);

        vendaDao = new JDBCVendaDAO(fabricaConexao);
        vendaRepositorio = new VendaRepositorio(vendaDao, clienteDao, produtoDao);

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }



    @Override
    public String getHome() {
        // TODO Auto-generated method stub
        return "PRINCIPAL";
    }

    @Override
    public String getAppTitle() {
        // TODO Auto-generated method stub
        return "Vendinha";
    }

    @Override
    public void registrarTelas() {
        registraTela("PRINCIPAL", new ScreenRegistryFXML(getClass(), "fxml/principal.fxml", (o)->new TelaPrincipal()));
        registraTela("CLIENTES", new ScreenRegistryFXML(getClass(), "fxml/clientes.fxml", (o)->new TelaClientes(clienteRepositorio)));  
        registraTela("PRODUTOS", new ScreenRegistryFXML(getClass(), "fxml/produtos.fxml", (o)->new TelaProdutos(produtoRepositorio)));
        registraTela("VENDAS", new ScreenRegistryFXML(getClass(), "fxml/vendas.fxml", (o)->new TelaVendas(vendaRepositorio)));
        registraTela("NOVAVENDA", new ScreenRegistryFXML(getClass(), "fxml/novavenda.fxml", (o)->new TelaNovaVenda(vendaRepositorio,clienteRepositorio,produtoRepositorio)));
          
    
    }


}