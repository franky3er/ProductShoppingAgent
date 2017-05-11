package vs.productshoppingagent.main;

import vs.shopservice.ShopService;
import vs.shopservice.ShopServiceClientFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private static String shopServiceProductsXMLSource;

    private static List<ShopService.Client> clients;

    public static void main(String[] args) {
        try {
            loadConfig();
            initialize();
            run();
        } catch (IOException e) {
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
        shopServiceProductsXMLSource = properties.getProperty(PRODUCTSHOPPINGAGENT_SHOPSERVICE_PRODUCTSREFILLINFO_XMLSOURCE);
    }

    private static void initialize() {
        initializeShopServiceClients();
        initializeProductsRefillInfo();
    }

    private static void initializeShopServiceClients() {
        System.out.println("INFO : Initializing ShopServiceClients...");
        clients = ShopServiceClientFactory.createClientsFromXML(shopServiceClientsXmlSource);
    }

    private static void initializeProductsRefillInfo() {
        //TODO initialize the information for all products when to refill etc
    }

    private static void run() {
        //TODO implement run
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
