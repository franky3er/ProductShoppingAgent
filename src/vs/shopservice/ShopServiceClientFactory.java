package vs.shopservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create clients.
 */
public class ShopServiceClientFactory {

    /**
     * Parses the xml source to create clients from the informatin within.
     *
     * @param xmlSource
     * @return List<ShopService.Client>
     */
    public static List<ShopService.Client> createClientsFromXML(String xmlSource) {
        List<ShopService.Client> clients = new ArrayList<>();
        //TODO Parse xml source
        //TODO Generate clients
        return clients;
    }
}
