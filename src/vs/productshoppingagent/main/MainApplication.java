package vs.productshoppingagent.main;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import vs.products.iohandler.database.ProductDatabaseHandler;
import vs.products.refillinfo.ProductRefillInfo;
import vs.products.refillinfo.ProductRefillInfoFactory;
import vs.products.ProductShoppingAgent;
import vs.shopservice.ShopService;
import vs.shopservice.ShopServiceClientFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Main Application
 */
public class MainApplication {
    private final static String PROJECT_NAME = "ProductShoppingAgent";
    private final static String PROJECT_CONFIG = System.getProperty("user.dir") + File.separator +
            "config" + File.separator + PROJECT_NAME + ".properties";

    private final static String PRODUCTSHOPPINGAGENT_SHOPSERVICE_SERVERS_JSONSOURCE = "ProductShoppingAgent.ShopService.Servers.JSONSource";
    private final static String PRODUCTSHOPPINGAGENT_SHOPSERVICE_PRODUCTSREFILLINFO_XMLSOURCE = "ProductShoppingAgent.ShopService.ProductsRefillInfo.XMLSource";
    private final static String PRODUCTSHOPPINGAGENT_PRODUCTDB_DRIVER = "ProductShoppingAgent.ProductDB.Driver";
    private final static String PRODUCTSHOPPINGAGENT_PRODUCTDB_FILESOURCE = "ProductShoppingAgent.ProductDB.FileSource";
    private final static String PRODUCTSHOPPINGAGENT_DELIVERY_ADDRESS = "ProductShoppingAgent.Delivery.Address";

    private static String shopServiceServersJsonSource;
    private static String shopServiceProductsRefilInfoXMLSource;
    private static String productDBDriver;
    private static String productDBFileSource;
    private static String deliveryAddress;

    private static Map<String, ShopService.Client> clients;
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
            System.err.println(String.format("ERROR : Unable to parse xml file", shopServiceProductsRefilInfoXMLSource));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(String.format("ERROR : Driver not found: %s", productDBDriver));
            e.printStackTrace();
        } catch (TTransportException e) {
            System.err.println("ERROR : Connection to shop failed");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println(String.format("ERROR : Parsing JSON file %s failed", shopServiceServersJsonSource));
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
        shopServiceServersJsonSource = properties.getProperty(PRODUCTSHOPPINGAGENT_SHOPSERVICE_SERVERS_JSONSOURCE);
        shopServiceProductsRefilInfoXMLSource = properties.getProperty(PRODUCTSHOPPINGAGENT_SHOPSERVICE_PRODUCTSREFILLINFO_XMLSOURCE);
        productDBDriver = properties.getProperty(PRODUCTSHOPPINGAGENT_PRODUCTDB_DRIVER);
        productDBFileSource = properties.getProperty(PRODUCTSHOPPINGAGENT_PRODUCTDB_FILESOURCE);
        deliveryAddress = properties.getProperty(PRODUCTSHOPPINGAGENT_DELIVERY_ADDRESS);
    }

    private static void initialize() throws SQLException, IOException, SAXException, ClassNotFoundException, ParseException, TTransportException {
        initializeShopServiceClients();
        initializeProductsRefillInfo();
        initializeProductDB();
        initializeProductShoppingAgent();
    }

    private static void initializeShopServiceClients() throws ParseException, IOException, TTransportException {
        System.out.println("INFO : Initializing ShopServiceClients : " + shopServiceServersJsonSource);
        clients = ShopServiceClientFactory.createClientsFromJSON(shopServiceServersJsonSource);
    }

    private static void initializeProductsRefillInfo() throws IOException, SAXException {
        System.out.println("INFO : Initializing ProductsRefillInfo : " + shopServiceProductsRefilInfoXMLSource);
        productsRefillInfos = ProductRefillInfoFactory.createProductsRefillInfoFromXML(shopServiceProductsRefilInfoXMLSource);
    }

    private static void initializeProductDB() throws SQLException, ClassNotFoundException {
        Class.forName(productDBDriver);
        connection = DriverManager.getConnection("jdbc:sqlite:" + productDBFileSource);
        ProductDatabaseHandler productDatabaseHandler = new ProductDatabaseHandler(connection);
        productDatabaseHandler.createTablesIfNotExist();
    }

    private static void initializeProductShoppingAgent() {
        productShoppingAgent = new ProductShoppingAgent(connection, clients,
                productsRefillInfos, deliveryAddress);
    }

    private static void run() {
        productShoppingAgent.refill();
    }


    private static void closeClients() {
        System.out.println("INFO : Close ShopService clients..");
        if (clients != null) {
            for (ShopService.Client client : clients.values()) {
                client.getInputProtocol().getTransport().close();
            }
        }
    }

    private static void closeProductDBConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("ERROR : Close product DB connection failed");
            e.printStackTrace();
        }
    }
}
