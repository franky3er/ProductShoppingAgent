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

    private static String shopServiceClientsXmlSource;
    private static String shopServiceProductsRefilInfoXMLSource;

    private static List<ShopService.Client> clients;
    private static List<ProductRefillInfo> productsRefillInfos;
    private static ProductShoppingAgent productShoppingAgent;

    public static void main(String[] args) {
        try {
            loadConfig();
            initialize();
            run();
        } catch (IOException e) {
            System.err.println("ERROR : IOException");
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("ERROR : Unable to parse xml");
            e.printStackTrace();
        } finally {
            closeClients();
        }
    }

    private static void loadConfig() throws IOException {
        System.out.println("INFO : Loading config...");
        Properties properties = new Properties();
        properties.load(new FileReader(PROJECT_CONFIG));
        shopServiceClientsXmlSource = properties.getProperty(PRODUCTSHOPPINGAGENT_SHOPSERVICE_CLIENTS_XMLSOURCE);
        shopServiceProductsRefilInfoXMLSource = properties.getProperty(PRODUCTSHOPPINGAGENT_SHOPSERVICE_PRODUCTSREFILLINFO_XMLSOURCE);
     }

    private static void initialize() throws IOException, SAXException {
        initializeShopServiceClients();
        initializeProductsRefillInfo();
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

    private static void initializeProductShoppingAgent() {
        productShoppingAgent = new ProductShoppingAgent(clients, productsRefillInfos);
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
}
