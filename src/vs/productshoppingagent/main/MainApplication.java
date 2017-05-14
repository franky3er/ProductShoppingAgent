package vs.productshoppingagent.main;

import org.xml.sax.SAXException;
import vs.product.refillinfo.ProductRefillInfo;
import vs.product.refillinfo.ProductRefillInfoFactory;
import vs.product.ProductShoppingAgent;
import vs.shopservice.ShopService;
import vs.shopservice.ShopServiceClientFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Main Application
 */
public class MainApplication {
    private final static String PROJECT_NAME = "ProductShoppingAgent";
    private final static String PROJECT_CONFIG = System.getProperty("user.dir") + File.separator +
            "config" + File.separator + PROJECT_NAME + ".properties";

    private final static String PRODUCTSHOPPINGAGENT_SHOPSERVICE_CLIENTS_XMLSOURCE = "ProductShoppingAgent.ShopService.Clients.XMLSource";
    private final static String PRODUCTSHOPPINGAGENT_SHOPSERVICE_PRODUCTSREFILLINFO_XMLSOURCE = "ProductShoppingAgent.ShopService.ProductsRefillInfo.XMLSource";
    private final static String PRODUCTSHOPPINGAGENT_PRODUCTDB_DRIVER = "ProductShoppingAgent.ProductDB.Driver";
    private final static String PRODUCTSHOPPINGAGENT_PRODUCTDB_FILESOURCE = "ProductShoppingAgent.ProductDB.FileSource";

    private static String shopServiceClientsXmlSource;
    private static String shopServiceProductsRefilInfoXMLSource;
    private static String productDBDriver;
    private static String productDBFileSource;

    private static List<ShopService.Client> clients;
    private static List<ProductRefillInfo> productsRefillInfos;
    private static ProductShoppingAgent productShoppingAgent;
    private static Connection connection;

    public static void main(String[] args) {
        try {
            loadConfig();
            initialize();
            run();
        } catch (IOException e) {
            System.err.println("ERROR : IOException");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(String.format("ERROR : Connection to product DB failed : %s", productDBFileSource));
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("ERROR : Unable to parse xml");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(String.format("ERROR : Driver not found: %s", productDBDriver));
            e.printStackTrace();
        } finally {
            closeClients();
            closeProductDBConnection();
        }
    }

    private static void loadConfig() throws IOException {
        System.out.println("INFO : Loading config...");
        Properties properties = new Properties();
        properties.load(new FileReader(PROJECT_CONFIG));
        shopServiceClientsXmlSource = properties.getProperty(PRODUCTSHOPPINGAGENT_SHOPSERVICE_CLIENTS_XMLSOURCE);
        shopServiceProductsRefilInfoXMLSource = properties.getProperty(PRODUCTSHOPPINGAGENT_SHOPSERVICE_PRODUCTSREFILLINFO_XMLSOURCE);
        productDBDriver = properties.getProperty(PRODUCTSHOPPINGAGENT_PRODUCTDB_DRIVER);
        productDBFileSource = properties.getProperty(PRODUCTSHOPPINGAGENT_PRODUCTDB_FILESOURCE);
    }

    private static void initialize() throws SQLException, IOException, SAXException, ClassNotFoundException {
        initializeShopServiceClients();
        initializeProductsRefillInfo();
        initializeProductDB();
        initializeProductShoppingAgent();
    }

    private static void initializeShopServiceClients() {
        System.out.println("INFO : Initializing ShopServiceClients : " + shopServiceClientsXmlSource);
        clients = ShopServiceClientFactory.createClientsFromXML(shopServiceClientsXmlSource);
    }

    private static void initializeProductsRefillInfo() throws IOException, SAXException {
        System.out.println("INFO : Initializing ProductsRefillInfo : " + shopServiceProductsRefilInfoXMLSource);
        productsRefillInfos = ProductRefillInfoFactory.createProductsRefillInfoFromXML(shopServiceProductsRefilInfoXMLSource);
    }

    private static void initializeProductDB() throws SQLException, ClassNotFoundException {
        Class.forName(productDBDriver);
        connection = DriverManager.getConnection("jdbc:sqlite:" + productDBFileSource);
    }

    private static void initializeProductShoppingAgent() {
        productShoppingAgent = new ProductShoppingAgent(connection, clients, productsRefillInfos);
    }

    private static void run() {
        productShoppingAgent.purchase();
    }


    private static void closeClients() {
        System.out.println("INFO : Close ShopService clients..");
        if (clients != null) {
            for (ShopService.Client client : clients) {
                client.getInputProtocol().getTransport().close();
            }
        }
    }

    private static void closeProductDBConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("ERROR : Close product DB connection failed");
            e.printStackTrace();
        }
    }
}
